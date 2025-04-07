package infrastructure.db;

import java.sql.*;
import java.util.Map;

public class SQLiteGenreRepository implements GenreRepository {
    private static final String DB_URL = DatabaseConfig.getConnectionUrl();

    @Override
    public void saveGenres(Map<Integer, String> genres) {
        String sql = "INSERT OR IGNORE INTO genres (id, name) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
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
}