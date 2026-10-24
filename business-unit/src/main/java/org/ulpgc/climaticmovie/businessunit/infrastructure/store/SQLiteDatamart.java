package org.ulpgc.climaticmovie.businessunit.infrastructure.store;

import org.ulpgc.climaticmovie.businessunit.domain.models.*;

import java.sql.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

public class SQLiteDatamart {
    private final String dbUrl;

    public SQLiteDatamart(String dbUrl) {
        this.dbUrl = dbUrl;
        initializeDatabase();
    }

    private void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(dbUrl);
             Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS weather (" +
                    "city TEXT PRIMARY KEY COLLATE NOCASE, " +
                    "temperature REAL, " +
                    "humidity INTEGER, " +
                    "condition TEXT, " +
                    "timestamp DATETIME)");
            stmt.execute("CREATE TABLE IF NOT EXISTS movies (" +
                    "title TEXT PRIMARY KEY, " +
                    "releaseDate TEXT, " +
                    "voteAverage REAL, " +
                    "genres TEXT)");
            stmt.execute("CREATE TABLE IF NOT EXISTS recommendations (" +
                    "city TEXT PRIMARY KEY COLLATE NOCASE, " +
                    "weather_condition TEXT, " +
                    "recommended_movies TEXT)");
        } catch (SQLException e) {
            throw new IllegalStateException("Error inicializando base de datos: " + e.getMessage(), e);
        }
    }

    public void upsertWeatherData(List<Weather> weatherData) {
        inTransaction(conn -> {
            try (PreparedStatement insertStmt = conn.prepareStatement(
                    "INSERT OR REPLACE INTO weather VALUES (?, ?, ?, ?, ?)")) {
                for (Weather weather : weatherData) {
                    insertStmt.setString(1, weather.getCity());
                    insertStmt.setDouble(2, weather.getTemperature());
                    insertStmt.setInt(3, weather.getHumidity());
                    insertStmt.setString(4, weather.getCondition());
                    insertStmt.setString(5, weather.getTimestamp().toString());
                    insertStmt.addBatch();
                }
                insertStmt.executeBatch();
            }
        }, "Error actualizando datos de clima");
    }

    public void replaceMovies(List<Movie> movies) {
        inTransaction(conn -> {
            try (Statement clearStmt = conn.createStatement();
                 PreparedStatement insertStmt = conn.prepareStatement(
                         "INSERT OR REPLACE INTO movies (title, releaseDate, voteAverage, genres) VALUES (?, ?, ?, ?)")) {
                clearStmt.execute("DELETE FROM movies");
                for (Movie movie : movies) {
                    insertStmt.setString(1, movie.getTitle());
                    insertStmt.setString(2, movie.getReleaseDate() != null ? movie.getReleaseDate().toString() : null);
                    insertStmt.setDouble(3, movie.getVoteAverage());
                    insertStmt.setString(4, String.join(",", movie.getGenres()));
                    insertStmt.addBatch();
                }
                insertStmt.executeBatch();
            }
        }, "Error actualizando datos de películas");
    }

    public void updateRecommendations(List<Recommendation> recommendations) {
        inTransaction(conn -> {
            try (Statement clearStmt = conn.createStatement();
                 PreparedStatement insertStmt = conn.prepareStatement(
                         "INSERT OR REPLACE INTO recommendations VALUES (?, ?, ?)")) {
                clearStmt.execute("DELETE FROM recommendations");
                for (Recommendation rec : recommendations) {
                    insertStmt.setString(1, rec.getCity());
                    insertStmt.setString(2, rec.getWeatherCondition());
                    insertStmt.setString(3, String.join("|", rec.getRecommendedMovies()));
                    insertStmt.addBatch();
                }
                insertStmt.executeBatch();
            }
        }, "Error actualizando recomendaciones");
    }

    public List<Weather> getAllWeatherData() {
        List<Weather> weatherList = new ArrayList<>();
        String query = "SELECT city, temperature, humidity, condition, timestamp FROM weather";
        try (Connection conn = DriverManager.getConnection(dbUrl);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                weatherList.add(new Weather(
                        rs.getString("city"),
                        rs.getDouble("temperature"),
                        rs.getInt("humidity"),
                        rs.getString("condition"),
                        Instant.parse(rs.getString("timestamp"))
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error obteniendo datos de clima: " + e.getMessage());
        }
        return weatherList;
    }

    public List<Movie> getAllMovies() {
        List<Movie> movieList = new ArrayList<>();
        String query = "SELECT title, releaseDate, voteAverage, genres FROM movies";
        try (Connection conn = DriverManager.getConnection(dbUrl);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                String genres = rs.getString("genres");
                movieList.add(new Movie(
                        rs.getString("title"),
                        parseDate(rs.getString("releaseDate")),
                        rs.getDouble("voteAverage"),
                        genres == null || genres.isEmpty() ? List.of() : Arrays.asList(genres.split(","))
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error obteniendo películas: " + e.getMessage());
        }
        return movieList;
    }

    public List<String> getAvailableCities() {
        List<String> cities = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(dbUrl);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT city FROM recommendations ORDER BY city")) {
            while (rs.next()) {
                cities.add(rs.getString("city"));
            }
        } catch (SQLException e) {
            System.err.println("Error obteniendo ciudades: " + e.getMessage());
        }
        return cities;
    }

    public Recommendation getRecommendationForCity(String city) {
        String query = "SELECT city, weather_condition, recommended_movies FROM recommendations WHERE city = ?";
        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, city.trim());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String movies = rs.getString("recommended_movies");
                    return new Recommendation(
                            rs.getString("city"),
                            rs.getString("weather_condition"),
                            movies == null || movies.isEmpty() ? List.of() : Arrays.asList(movies.split("\\|"))
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error obteniendo recomendación: " + e.getMessage());
        }
        return null;
    }

    private static LocalDate parseDate(String value) {
        if (value == null || value.isBlank()) return null;
        try {
            return LocalDate.parse(value);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private interface SqlWork {
        void run(Connection conn) throws SQLException;
    }

    private void inTransaction(SqlWork work, String errorMessage) {
        try (Connection conn = DriverManager.getConnection(dbUrl)) {
            conn.setAutoCommit(false);
            try {
                work.run(conn);
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            System.err.println(errorMessage + ": " + e.getMessage());
        }
    }
}
