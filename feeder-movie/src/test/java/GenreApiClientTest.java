import infrastructure.Config;
import infrastructure.api.GenreApiClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class GenreApiClientTest {
    private GenreApiClient client;
    private final String REAL_API_KEY = Config.getApiKey(); // Reemplaza con una key válida

    @BeforeEach
    void setUp() {
        client = new GenreApiClient(REAL_API_KEY);
    }

    // Verifica que la API devuelva un mapa de géneros no vacío con IDs válidos
    @Test
    void fetchGenres_shouldReturnNonEmptyMap() {
        // Act
        Map<Integer, String> genres = client.fetchGenres();
        // Assert
        assertFalse(genres.isEmpty());
        assertTrue(genres.containsKey(28)); // ID común para "Action"
    }

    // Confirma que los nombres de géneros estén correctamente traducidos al español
    @Test
    void fetchGenres_shouldContainSpanishNames() {
        Map<Integer, String> genres = client.fetchGenres();
        assertEquals("Acción", genres.get(28)); // Verifica traducción al español
    }

    // Valida el manejo adecuado de errores cuando se usa una API key inválida
    @Test
    void fetchGenres_shouldHandleApiErrorsGracefully() {
        GenreApiClient invalidClient = new GenreApiClient("key_invalida");

        assertThrows(RuntimeException.class, invalidClient::fetchGenres);
    }
}
