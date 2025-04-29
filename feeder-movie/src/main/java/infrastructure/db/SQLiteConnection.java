package infrastructure.db;

import core.Movie;
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
                    id INTEGER PRIMARY KEY,
                    title TEXT,
                    release_date TEXT,
                    vote_average REAL,
                    vote_count INTEGER,
                    genre_ids TEXT
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
            (id, title, release_date, vote_average, vote_count, genre_ids) 
            VALUES (?, ?, ?, ?, ?, ?)""";
        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);
            for (Movie movie : movies) {
                pstmt.setInt(1, movie.getId());
                pstmt.setString(2, movie.getTitle());
                pstmt.setString(3, movie.getReleaseDate());
                pstmt.setDouble(4, movie.getVoteAverage());
                pstmt.setInt(5, movie.getVoteCount());
                pstmt.setString(6, movie.getGenreIds().toString());
                pstmt.executeUpdate();
            }
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}