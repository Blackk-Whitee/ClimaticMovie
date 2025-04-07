import core.Movie;
import infrastructure.Config;
import infrastructure.api.MovieApiClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class MovieApiClientTest {
    private MovieApiClient client;
    private final String REAL_API_KEY = Config.getApiKey(); // Reemplaza con una key válida

    @BeforeEach
    void setUp() {
        client = new MovieApiClient(REAL_API_KEY);
    }

    // Verifica que la API devuelva exactamente 20 películas por página (estándar TMDb)
    @Test
    void fetchTrendingMovies_shouldReturn20MoviesPerPage() {
        List<Movie> movies = client.fetchTrendingMovies(1);
        assertEquals(20, movies.size()); // La API suele devolver 20 items/página
        assertNotNull(movies.get(0).getTitle());
    }

    // Confirma que cada película incluye al menos un género asociado
    @Test
    void fetchTrendingMovies_shouldIncludeGenreIds() {
        List<Movie> movies = client.fetchTrendingMovies(1);
        assertFalse(movies.get(0).getGenreIds().isEmpty());
    }

    // Valida que el cliente maneje correctamente solicitudes con número de página inválido
    @Test
    void fetchTrendingMovies_shouldHandleInvalidPage() {
        assertThrows(RuntimeException.class, () -> client.fetchTrendingMovies(-1));
    }
}
