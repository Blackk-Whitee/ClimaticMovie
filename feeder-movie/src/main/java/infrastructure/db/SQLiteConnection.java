package infrastructure.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteConnection {
    private static final String DB_URL = DatabaseConfig.getConnectionUrl();

    public static void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            // Crear tabla de géneros
            String createGenresTable = """
                CREATE TABLE IF NOT EXISTS genres (
                    id INTEGER PRIMARY KEY,
                    name TEXT NOT NULL
                )""";

            // Crear tabla de películas
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

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }
}
