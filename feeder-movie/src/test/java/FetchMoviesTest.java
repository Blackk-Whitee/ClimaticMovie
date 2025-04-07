import core.Movie;
import core.usecases.FetchMovies;
import infrastructure.api.MovieApiClient;
import infrastructure.db.MovieRepository;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class FetchMoviesTest {
    // Verifica que se obtengan y guarden películas de múltiples páginas
    @Test
    void execute_shouldFetchMoviesForAllPages() {
        MovieApiClient apiClient = new MovieApiClient("api_key_fake") {
            @Override
            public List<Movie> fetchTrendingMovies(int page) {
                return List.of(new Movie(page, "Movie " + page, "2023", 8.0, 100, List.of(1)));
            }
        };

        MovieRepository repository = movies -> {}; // No-op
        FetchMovies fetchMovies = new FetchMovies(apiClient, repository);

        List<Movie> result = fetchMovies.execute(3);
        assertEquals(3, result.size());
    }

    // Valida el manejo cuando la API devuelve lista vacía
    @Test
    void execute_shouldHandleEmptyPages() {
        MovieApiClient apiClient = new MovieApiClient("api_key_fake") {
            @Override
            public List<Movie> fetchTrendingMovies(int page) {
                return List.of();
            }
        };

        MovieRepository repository = movies -> assertTrue(movies.isEmpty());
        FetchMovies fetchMovies = new FetchMovies(apiClient, repository);

        List<Movie> result = fetchMovies.execute(1);
        assertTrue(result.isEmpty());
    }

    // Comprueba que se propaguen correctamente los errores de API
    @Test
    void execute_shouldPropagateApiErrors() {
        MovieApiClient apiClient = new MovieApiClient("api_key_fake") {
            @Override
            public List<Movie> fetchTrendingMovies(int page) {
                throw new RuntimeException("API error");
            }
        };

        MovieRepository repository = movies -> fail("No debería llamarse");
        FetchMovies fetchMovies = new FetchMovies(apiClient, repository);

        assertThrows(RuntimeException.class, () -> fetchMovies.execute(1));
    }
}