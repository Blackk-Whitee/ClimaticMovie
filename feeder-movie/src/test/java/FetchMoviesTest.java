import core.Movie;
import core.usecases.FetchMovies;
import infrastructure.api.MovieApiClient;
import infrastructure.db.SQLiteRepositoryInterface;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FetchMoviesTest {
    @Test
    void testExecute() {
        // Mock setup
        MovieApiClient mockApi = mock(MovieApiClient.class);
        SQLiteRepositoryInterface mockRepo = mock(SQLiteRepositoryInterface.class);

        List<Movie> testMovies = Arrays.asList(
                new Movie(1, "Movie 1", "2023-01-01", 8.5, 100, List.of(1, 2)),
                new Movie(2, "Movie 2", "2023-02-01", 7.8, 80, List.of(3))
        );

        when(mockApi.fetchTrendingMovies(anyInt())).thenReturn(testMovies);

        // Test execution
        FetchMovies fetcher = new FetchMovies(mockApi, mockRepo);
        List<Movie> result = fetcher.execute(1);

        // Verification
        assertEquals(testMovies.size(), result.size());
        verify(mockRepo, times(1)).saveMovies(testMovies);
    }
}