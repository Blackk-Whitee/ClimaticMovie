package infrastructure.api;
import domain.models.Movie;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class TmdbMovieProviderTest {
    @Test
    void testFetchTrendingMovies() {
        // Mock manual
        TmdbMovieProvider client = new TmdbMovieProvider("test_key") {
            @Override
            public List<Movie> fetchTrendingMovies(int page) {
                return List.of(
                        new Movie("Test Movie", "2023-01-01", 8.0, 100, List.of(1, 2))
                );
            }
        };

        List<Movie> result = client.fetchTrendingMovies(1);

        assertEquals(1, result.size());
        assertEquals("Test Movie", result.get(0).getTitle());
    }
}