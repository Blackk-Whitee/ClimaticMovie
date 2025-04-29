package infrastructure.api;
import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class GenreApiClientTest {
    @Test
    void testFetchGenres() {
        // Mock manual del API response
        GenreApiClient client = new GenreApiClient("test_key") {
            @Override
            public Map<Integer, String> fetchGenres() {
                return Map.of(
                        1, "Action",
                        2, "Comedy",
                        3, "Drama"
                );
            }
        };

        Map<Integer, String> result = client.fetchGenres();

        assertEquals(3, result.size());
        assertEquals("Comedy", result.get(2));
    }
}