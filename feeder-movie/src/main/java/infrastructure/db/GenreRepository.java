package infrastructure.db;

import java.util.Map;

public interface GenreRepository {
    void saveGenres(Map<Integer, String> genres);
}
