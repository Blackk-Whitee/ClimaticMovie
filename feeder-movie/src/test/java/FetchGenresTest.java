import core.usecases.FetchGenres;
import infrastructure.api.GenreApiClient;
import infrastructure.db.SQLiteRepositoryInterface;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FetchGenresTest {  // Nota: clase pública
    @Test
    void testExecute() {
        GenreApiClient mockApi = mock(GenreApiClient.class);
        SQLiteRepositoryInterface mockRepo = mock(SQLiteRepositoryInterface.class);

        Map<Integer, String> testGenres = new HashMap<>();
        testGenres.put(1, "Action");
        testGenres.put(2, "Comedy");

        when(mockApi.fetchGenres()).thenReturn(testGenres);

        FetchGenres fetcher = new FetchGenres(mockApi, mockRepo);
        Map<Integer, String> result = fetcher.execute();

        assertEquals(testGenres, result);
        verify(mockRepo, times(1)).saveGenres(testGenres);
    }
}