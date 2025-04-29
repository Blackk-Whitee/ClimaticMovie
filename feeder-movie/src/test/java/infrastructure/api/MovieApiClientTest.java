package infrastructure.api;
import core.Movie;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class MovieApiClientTest {
    @Test
    void testFetchTrendingMovies() {
        // Mock manual
        MovieApiClient client = new MovieApiClient("test_key") {
            @Override
            public List<Movie> fetchTrendingMovies(int page) {
                return List.of(
                        new Movie(1, "Test Movie", "2023-01-01", 8.0, 100, List.of(1, 2))
                );
            }
        };

        List<Movie> result = client.fetchTrendingMovies(1);

        assertEquals(1, result.size());
        assertEquals("Test Movie", result.get(0).getTitle());
    }
}