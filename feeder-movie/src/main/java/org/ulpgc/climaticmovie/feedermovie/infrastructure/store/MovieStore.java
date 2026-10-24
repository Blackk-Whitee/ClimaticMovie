package org.ulpgc.climaticmovie.feedermovie.infrastructure.store;

import org.ulpgc.climaticmovie.feedermovie.domain.models.Movie;

public interface MovieStore {

    void save(Movie movie);
}
