package core.usecases;

import core.Movie;
import infrastructure.api.MovieApiClient;
import infrastructure.db.MovieRepository;
import java.util.ArrayList;
import java.util.List;

public class FetchMovies {
    private final MovieApiClient apiClient;
    private final MovieRepository repository;

    public FetchMovies(MovieApiClient apiClient, MovieRepository repository) {
        this.apiClient = apiClient;
        this.repository = repository;
    }

    public List<Movie> execute(int pages) {
        List<Movie> movies = new ArrayList<>();
        for (int page = 1; page <= pages; page++) {
            movies.addAll(apiClient.fetchTrendingMovies(page));
        }
        repository.saveMovies(movies);
        return movies;
    }
}
