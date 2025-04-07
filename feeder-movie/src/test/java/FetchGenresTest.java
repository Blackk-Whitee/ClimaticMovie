import core.usecases.FetchGenres;
import infrastructure.api.GenreApiClient;
import infrastructure.db.GenreRepository;
import org.junit.jupiter.api.Test;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class FetchGenresTest {

    // Test 1: Flujo normal con géneros válidos
    @Test
    void execute_shouldReturnAndSaveGenres() {
        // Configuración
        GenreApiClient apiClient = new GenreApiClient("fake_key") {
            @Override
            public Map<Integer, String> fetchGenres() {
                return Map.of(1, "Action", 2, "Drama");
            }
        };

        final AtomicBoolean wasSaved = new AtomicBoolean(false);
        GenreRepository repository = genres -> {
            assertEquals(2, genres.size()); // Verificación durante el guardado
            wasSaved.set(true);
        };

        // Ejecución
        Map<Integer, String> result = new FetchGenres(apiClient, repository).execute();

        // Verificación
        assertEquals(2, result.size());
        assertTrue(wasSaved.get()); // Confirma que se llamó a saveGenres
    }

    // Test 2: Manejo de respuesta vacía
    @Test
    void execute_shouldHandleEmptyResponse() {
        GenreApiClient apiClient = new GenreApiClient("fake_key") {
            @Override
            public Map<Integer, String> fetchGenres() {
                return Map.of();
            }
        };

        final AtomicBoolean receivedEmpty = new AtomicBoolean(false);
        GenreRepository repository = genres -> {
            assertTrue(genres.isEmpty());
            receivedEmpty.set(true);
        };

        Map<Integer, String> result = new FetchGenres(apiClient, repository).execute();

        assertTrue(result.isEmpty());
        assertTrue(receivedEmpty.get());
    }

    // Test 3: Propagación de errores
    @Test
    void execute_shouldPropagateApiErrors() {
        GenreApiClient apiClient = new GenreApiClient("fake_key") {
            @Override
            public Map<Integer, String> fetchGenres() {
                throw new RuntimeException("API error");
            }
        };

        GenreRepository repository = genres -> fail("No debería llamarse a saveGenres");

        assertThrows(RuntimeException.class,
                () -> new FetchGenres(apiClient, repository).execute()
        );
    }
}