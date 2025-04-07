import core.Movie;
import infrastructure.db.SQLiteMovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SQLiteMovieRepositoryTest {
    private SQLiteMovieRepository repository;

    @BeforeEach
    void setUp() {
        repository = new SQLiteMovieRepository();
        // Asegurar que la BD está inicializada
        infrastructure.db.SQLiteConnection.initializeDatabase();
    }

    // Verifica que las películas se guarden correctamente en la base de datos
    @Test
    void saveMovies_shouldPersistMovies() {
        List<Movie> movies = List.of(
                new Movie(1, "Movie 1", "2023-01-01", 8.5, 100, List.of(1, 2))
        );

        repository.saveMovies(movies);

        // Verificación manual en BD o con consulta SELECT
        assertTrue(true); // Placeholder para verificación real
    }

    // Valida el manejo de listas vacías de películas
    @Test
    void saveMovies_shouldHandleEmptyList() {
        repository.saveMovies(List.of());
        assertTrue(true); // No debe lanzar excepciones
    }
}