package infrastructure.db;

import core.Movie;
import infrastructure.store.SQLiteConnection;
import org.junit.jupiter.api.*;
import java.sql.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class SQLiteConnectionTest {
    private static final String TEST_DB = "jdbc:sqlite::memory:";
    private SQLiteConnection db;

    @BeforeEach
    void setUp() throws SQLException {
        db = new SQLiteConnection(TEST_DB);
        db.initializeDatabase();
    }

    @Test
    void testDatabaseInitialization() throws SQLException {
        try (Connection conn = db.getConnection();
             ResultSet rs = conn.getMetaData().getTables(null, null, "%", null)) {

            List<String> tables = new ArrayList<>();
            while (rs.next()) tables.add(rs.getString("TABLE_NAME"));

            assertTrue(tables.contains("genres"));
            assertTrue(tables.contains("movies"));
        }
    }

    @Test
    void testSaveAndRetrieveGenres() throws SQLException {
        // Datos de prueba
        Map<Integer, String> testGenres = Map.of(1, "Action", 2, "Comedy");

        // Guardar
        db.saveGenres(testGenres);

        // Verificar
        try (Connection conn = db.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id, name FROM genres")) {

            Map<Integer, String> savedGenres = new HashMap<>();
            while (rs.next()) savedGenres.put(rs.getInt("id"), rs.getString("name"));

            assertEquals(testGenres, savedGenres);
        }
    }

    @Test
    void testSaveAndRetrieveMovies() throws SQLException {
        // Datos de prueba
        Movie movie = new Movie("Inception", "2010-07-16", 8.8, 25000, List.of(1, 2));

        // Guardar
        db.saveMovies(List.of(movie));

        // Verificar
        try (Connection conn = db.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM movies WHERE id = 1")) {

            assertTrue(rs.next());
            assertEquals(movie.getTitle(), rs.getString("title"));
            assertEquals(movie.getReleaseDate(), rs.getString("release_date"));
            assertEquals(movie.getVoteAverage(), rs.getDouble("vote_average"), 0.01);
            assertEquals(movie.getVoteCount(), rs.getInt("vote_count"));
            assertEquals("[1, 2]", rs.getString("genre_ids"));
        }
    }
}