package infrastructure.store;

import domain.models.Movie;

public interface MovieStore {

    void save(Movie movie);
}
