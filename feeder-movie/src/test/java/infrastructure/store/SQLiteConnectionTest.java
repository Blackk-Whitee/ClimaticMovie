package infrastructure.store;

import domain.models.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class SQLiteConnectionTest {

    private SQLiteConnection sqlite;

    @BeforeEach
    void setUp() {
        // Usamos una base de datos en memoria para que no persista entre tests
        sqlite = new SQLiteConnection("jdbc:sqlite:test-db.sqlite");
        sqlite.initializeDatabase();
    }

    @Test
    void initializeDatabase_createsTables() throws SQLException {
        try (Connection conn = sqlite.getConnection();
             ResultSet rs = conn.getMetaData().getTables(null, null, null, null)) {

            boolean hasGenres = false;
            boolean hasMovies = false;

            while (rs.next()) {
                String tableName = rs.getString("TABLE_NAME");
                if ("genres".equalsIgnoreCase(tableName)) hasGenres = true;
                if ("movies".equalsIgnoreCase(tableName)) hasMovies = true;
            }

            assertTrue(hasGenres, "Table 'genres' should exist");
            assertTrue(hasMovies, "Table 'movies' should exist");
        }
    }

    @Test
    void saveGenres_insertsData() throws SQLException {
        Map<Integer, String> genres = Map.of(
                1, "Action",
                2, "Comedy"
        );

        sqlite.saveGenres(genres);

        try (Connection conn = sqlite.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM genres");
             ResultSet rs = stmt.executeQuery()) {

            int count = 0;
            while (rs.next()) {
                count++;
            }

            assertEquals(2, count, "Should insert 2 genres");
        }
    }

    @Test
    void saveMovies_insertsData() throws SQLException {
        Movie movie = new Movie(
                "topic",
                "2024-01-01T00:00:00Z",  // <-- Timestamp válido en ISO-8601
                "ss",
                "Test Movie",
                "2024-01-01",
                8.5,
                List.of("Drama", "Sci-Fi")
        );        sqlite.saveMovies(List.of(movie));

        try (Connection conn = sqlite.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM movies WHERE title = ?");
        ) {
            stmt.setString(1, "Test Movie");
            try (ResultSet rs = stmt.executeQuery()) {
                assertTrue(rs.next(), "Movie should be inserted");
                assertEquals("2024-01-01", rs.getString("release_date"));
                assertEquals(8.5, rs.getDouble("vote_average"));
                assertEquals("Drama,Sci-Fi", rs.getString("genres"));
            }
        }
    }
}
