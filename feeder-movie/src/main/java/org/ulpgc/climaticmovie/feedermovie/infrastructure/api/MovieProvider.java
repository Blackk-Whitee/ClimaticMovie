package org.ulpgc.climaticmovie.feedermovie.infrastructure.api;

import org.ulpgc.climaticmovie.feedermovie.domain.models.Movie;

import java.util.List;

public interface MovieProvider {
    List<Movie> provide();
}
