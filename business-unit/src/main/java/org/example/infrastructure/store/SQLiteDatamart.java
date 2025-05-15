package org.example.infrastructure.store;

import org.example.domain.models.*;
import java.sql.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

public class SQLiteDatamart {
    private static final String DB_URL = "jdbc:sqlite:datamart.db";

    public SQLiteDatamart() {
        initializeDatabase();
    }

    private void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            // Tabla de clima
            stmt.execute("CREATE TABLE IF NOT EXISTS weather (" +
                    "city TEXT PRIMARY KEY, " +
                    "temperature REAL, " +
                    "humidity INTEGER, " +
                    "condition TEXT, " +
                    "timestamp DATETIME)");

            // Tabla de películas
            stmt.execute("CREATE TABLE IF NOT EXISTS movies (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "title TEXT, " +
                    "releaseDate TEXT, " +
                    "voteAverage REAL, " +
                    "genres TEXT)");

            // Tabla de recomendaciones
            stmt.execute("CREATE TABLE IF NOT EXISTS recommendations (" +
                    "city TEXT PRIMARY KEY, " +
                    "weather_condition TEXT, " +
                    "recommended_movies TEXT)");

        } catch (SQLException e) {
            System.err.println("Error inicializando base de datos: " + e.getMessage());
        }
    }

    public void updateWeatherData(List<Weather> weatherData) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement clearStmt = conn.prepareStatement("DELETE FROM weather");
             PreparedStatement insertStmt = conn.prepareStatement(
                     "INSERT OR REPLACE INTO weather VALUES (?, ?, ?, ?, ?)")) {

            // Limpiar tabla existente
            clearStmt.execute();

            // Insertar nuevos datos
            for (Weather weather : weatherData) {
                insertStmt.setString(1, weather.getCity());
                insertStmt.setDouble(2, weather.getTemperature());
                insertStmt.setInt(3, weather.getHumidity());
                insertStmt.setString(4, weather.getCondition());
                insertStmt.setString(5, weather.getTimestamp().toString());
                insertStmt.addBatch();
            }
            insertStmt.executeBatch();

        } catch (SQLException e) {
            System.err.println("Error actualizando datos de clima: " + e.getMessage());
        }
    }

    public void updateRecommendations(List<Recommendation> recommendations) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement clearStmt = conn.prepareStatement("DELETE FROM recommendations");
             PreparedStatement insertStmt = conn.prepareStatement(
                     "INSERT OR REPLACE INTO recommendations VALUES (?, ?, ?)")) {

            // Limpiar tabla existente
            clearStmt.execute();

            // Insertar nuevas recomendaciones
            for (Recommendation rec : recommendations) {
                insertStmt.setString(1, rec.getCity());
                insertStmt.setString(2, rec.getWeatherCondition());
                insertStmt.setString(3, String.join("|", rec.getRecommendedMovies()));
                insertStmt.addBatch();
            }
            insertStmt.executeBatch();

        } catch (SQLException e) {
            System.err.println("Error actualizando recomendaciones: " + e.getMessage());
        }
    }

    public void updateMovies(List<Movie> movies) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement clearStmt = conn.prepareStatement("DELETE FROM movies");
             PreparedStatement insertStmt = conn.prepareStatement(
                     "INSERT INTO movies (title, releaseDate, voteAverage, genres) VALUES (?, ?, ?, ?)")) {

            // Limpiar tabla existente
            clearStmt.execute();

            // Insertar nuevos datos
            for (Movie movie : movies) {
                insertStmt.setString(1, movie.getTitle());
                insertStmt.setString(2, movie.getReleaseDate().toString());
                insertStmt.setDouble(3, movie.getVoteAverage());
                insertStmt.setString(4, String.join(",", movie.getGenres()));
                insertStmt.addBatch();
            }
            insertStmt.executeBatch();

        } catch (SQLException e) {
            System.err.println("Error actualizando datos de películas: " + e.getMessage());
        }
    }

    public List<Weather> getAllWeatherData() {
        List<Weather> weatherList = new ArrayList<>();
        String query = "SELECT city, temperature, humidity, condition, timestamp FROM weather";

        try (Connection conn = DriverManager.getConnection(DB_URL);
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

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                movieList.add(new Movie(
                        rs.getString("title"),
                        LocalDate.parse(rs.getString("releaseDate")),
                        rs.getDouble("voteAverage"),
                        Arrays.asList(rs.getString("genres").split(","))
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error obteniendo películas: " + e.getMessage());
        }
        return movieList;
    }

    public Recommendation getRecommendationForCity(String city) {
        String query = "SELECT city, weather_condition, recommended_movies FROM recommendations WHERE city = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, city);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Recommendation(
                        rs.getString("city"),
                        rs.getString("weather_condition"),
                        Arrays.asList(rs.getString("recommended_movies").split("\\|"))
                );
            }
        } catch (SQLException e) {
            System.err.println("Error obteniendo recomendación: " + e.getMessage());
        }
        return null;
    }
}