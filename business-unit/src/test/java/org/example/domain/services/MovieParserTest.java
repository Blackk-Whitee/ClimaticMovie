package org.example.domain.services;

import org.example.domain.models.Movie;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MovieParserTest {

    private final MovieParser parser = new MovieParser();

    @Test
    void parse_shouldReturnMovieObject_givenValidJson() {
        String json = """
            {
              "title": "Inception",
              "releaseDate": "2010-07-16",
              "voteAverage": 8.8,
              "genres": ["Action", "Sci-Fi"]
            }
        """;

        Movie movie = parser.parse(json);

        assertNotNull(movie);
        assertEquals("Inception", movie.getTitle());
        assertEquals(LocalDate.of(2010, 7, 16), movie.getReleaseDate());
        assertEquals(8.8, movie.getVoteAverage());
        assertEquals(List.of("Action", "Sci-Fi"), movie.getGenres());
    }

    @Test
    void parse_shouldReturnNull_givenInvalidJson() {
        String invalidJson = "esto no es un JSON válido";

        Movie result = parser.parse(invalidJson);

        assertNull(result);
    }

    @Test
    void parse_shouldReturnNull_givenInvalidDateFormat() {
        String badDateJson = """
            {
              "title": "Matrix",
              "releaseDate": "16-07-2010", 
              "voteAverage": 7.5,
              "genres": ["Sci-Fi"]
            }
        """;

        Movie result = parser.parse(badDateJson);

        assertNull(result);
    }

    @Test
    void parse_shouldIgnoreExtraFields() {
        String jsonWithExtras = """
            {
              "title": "The Prestige",
              "releaseDate": "2006-10-20",
              "voteAverage": 8.5,
              "genres": ["Drama", "Mystery"],
              "director": "Christopher Nolan"
            }
        """;

        Movie movie = parser.parse(jsonWithExtras);

        assertNotNull(movie);
        assertEquals("The Prestige", movie.getTitle());
        assertEquals(List.of("Drama", "Mystery"), movie.getGenres());
    }
}
