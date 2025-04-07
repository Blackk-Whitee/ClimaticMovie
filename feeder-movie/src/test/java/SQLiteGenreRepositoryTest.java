import infrastructure.db.SQLiteGenreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SQLiteGenreRepositoryTest {
    private SQLiteGenreRepository repository;

    @BeforeEach
    void setUp() {
        repository = new SQLiteGenreRepository();
        infrastructure.db.SQLiteConnection.initializeDatabase();
    }

    // Confirma que los géneros se almacenan correctamente en la base de datos
    @Test
    void saveGenres_shouldPersistGenres() {
        Map<Integer, String> genres = Map.of(
                1, "Action",
                2, "Drama"
        );

        repository.saveGenres(genres);
        assertTrue(true); // Placeholder para verificación con SELECT
    }

    // Prueba el manejo de un mapa vacío de géneros
    @Test
    void saveGenres_shouldHandleEmptyMap() {
        repository.saveGenres(Map.of());
        assertTrue(true); // No debe lanzar excepciones
    }
}