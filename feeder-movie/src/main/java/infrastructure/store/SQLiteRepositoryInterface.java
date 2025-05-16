package infrastructure.store;

import domain.models.Movie;
import java.util.List;
import java.util.Map;

public interface SQLiteRepositoryInterface {
    void saveGenres(Map<Integer, String> genres);
    void saveMovies(List<Movie> movies);
}