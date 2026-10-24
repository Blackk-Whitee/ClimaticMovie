package org.ulpgc.climaticmovie.feedermovie.application.controller;

import org.junit.jupiter.api.Test;
import org.ulpgc.climaticmovie.feedermovie.domain.models.Movie;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ControlTest {

    private static Movie movie(String title) {
        return new Movie("movies.Trending", "2025-05-18T12:00:00Z", "feeder movie", title, "2025-01-01", 7.0, List.of("Drama"));
    }

    @Test
    void fetchAndSend_skipsDuplicatedTitles() {
        List<Movie> saved = new ArrayList<>();
        Control control = new Control(() -> List.of(movie("A"), movie("B"), movie("A")), saved::add);

        int sent = control.fetchAndSend();

        assertEquals(2, sent);
        assertEquals(List.of("A", "B"), saved.stream().map(Movie::getTitle).toList());
    }
}
