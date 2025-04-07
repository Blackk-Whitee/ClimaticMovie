package infrastructure.db;

import core.Movie;
import java.util.List;

public interface MovieRepository {
    void saveMovies(List<Movie> movies);
}
