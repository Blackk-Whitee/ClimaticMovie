package infrastructure.api;

import core.Movie;

import java.util.List;

public interface MovieProvider {
    public List<Movie> provide();
}
