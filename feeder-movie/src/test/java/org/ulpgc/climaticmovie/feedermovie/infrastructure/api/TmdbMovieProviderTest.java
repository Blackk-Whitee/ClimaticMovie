package org.ulpgc.climaticmovie.feedermovie.infrastructure.api;

import org.junit.jupiter.api.Test;
import org.ulpgc.climaticmovie.feedermovie.domain.models.Movie;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TmdbMovieProviderTest {

    private static final Map<Integer, String> GENRES = Map.of(28, "Acción", 35, "Comedia");

    @Test
    void parseMovies_mapsFieldsAndGenreNames() {
        String json = """
            {"results": [
              {"title": "Película A", "release_date": "2025-01-10", "vote_average": 7.5, "genre_ids": [28, 35]}
            ]}
            """;

        List<Movie> movies = TmdbMovieProvider.parseMovies(json, GENRES);

        assertEquals(1, movies.size());
        Movie movie = movies.get(0);
        assertEquals("Película A", movie.getTitle());
        assertEquals("2025-01-10", movie.getReleaseDate());
        assertEquals(7.5, movie.getVoteAverage());
        assertEquals(List.of("Acción", "Comedia"), movie.getGenres());
        assertEquals("movies.Trending", movie.getTopic());
    }

    @Test
    void parseMovies_ignoresUnknownGenresAndMissingFields() {
        String json = """
            {"results": [
              {"title": "Sin fecha", "genre_ids": [99]},
              {"vote_average": 5.0}
            ]}
            """;

        List<Movie> movies = TmdbMovieProvider.parseMovies(json, GENRES);

        assertEquals(1, movies.size());
        assertEquals("", movies.get(0).getReleaseDate());
        assertTrue(movies.get(0).getGenres().isEmpty());
    }
}
