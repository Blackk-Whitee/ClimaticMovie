package core.usecases;

import infrastructure.api.GenreApiClient;
import infrastructure.db.GenreRepository;
import java.util.Map;

public class FetchGenres {
    private final GenreApiClient apiClient;
    private final GenreRepository repository;

    public FetchGenres(GenreApiClient apiClient, GenreRepository repository) {
        this.apiClient = apiClient;
        this.repository = repository;
    }

    public Map<Integer, String> execute() {
        Map<Integer, String> genres = apiClient.fetchGenres();
        repository.saveGenres(genres);
        return genres;
    }
}
