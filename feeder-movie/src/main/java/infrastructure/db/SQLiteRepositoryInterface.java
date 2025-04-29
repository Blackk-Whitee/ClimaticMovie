package infrastructure.db;

import core.Movie;
import java.util.List;
import java.util.Map;

public interface SQLiteRepositoryInterface {
    void saveGenres(Map<Integer, String> genres);
    void saveMovies(List<Movie> movies);
}