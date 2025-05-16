package infrastructure.api;

import domain.models.Movie;

import java.util.List;

public interface MovieProvider {
    public List<Movie> provide();
}
