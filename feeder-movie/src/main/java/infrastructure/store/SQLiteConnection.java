package infrastructure.store;

import domain.models.Movie;
import java.sql.*;
import java.util.List;
import java.util.Map;

public class SQLiteConnection implements SQLiteRepositoryInterface {
    private final String DB_URL;

    public SQLiteConnection(String dbUrl) {
        this.DB_URL = dbUrl;
    }

    public void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(this.DB_URL);
             Statement stmt = conn.createStatement()) {
            String createGenresTable = """
                CREATE TABLE IF NOT EXISTS genres (
                    id INTEGER PRIMARY KEY,
                    name TEXT NOT NULL
                )""";
            String createMoviesTable = """
                CREATE TABLE IF NOT EXISTS movies (
                    topic TEXT,
                    ts TEXT,
                    ss TEXT,
                    title TEXT PRIMARY KEY,
                    release_date TEXT,
                    vote_average REAL,
                    genres TEXT
                )""";
            stmt.execute(createGenresTable);
            stmt.execute(createMoviesTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(this.DB_URL);
    }

    @Override
    public void saveGenres(Map<Integer, String> genres) {
        String sql = "INSERT OR IGNORE INTO genres (id, name) VALUES (?, ?)";
        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);
            for (Map.Entry<Integer, String> entry : genres.entrySet()) {
                pstmt.setInt(1, entry.getKey());
                pstmt.setString(2, entry.getValue());
                pstmt.executeUpdate();
            }
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveMovies(List<Movie> movies) {
        String sql = """
            INSERT OR IGNORE INTO movies 
            (topic, ts, ss, title, release_date, vote_average, genres) 
            VALUES (?, ?, ?, ?, ?, ?, ?)""";
        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);
            for (Movie movie : movies) {
                pstmt.setString(1, movie.getTopic());
                pstmt.setString(2, String.valueOf(movie.getTs()));
                pstmt.setString(3, movie.getSs());
                pstmt.setString(4, movie.getTitle());
                pstmt.setString(5, movie.getReleaseDate());
                pstmt.setDouble(6, movie.getVoteAverage());
                pstmt.setString(7, String.join(",", movie.getGenreIds()));
                pstmt.executeUpdate();
            }
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}