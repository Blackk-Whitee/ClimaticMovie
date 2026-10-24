package org.ulpgc.climaticmovie.businessunit.domain.services;

import com.google.gson.Gson;
import org.ulpgc.climaticmovie.businessunit.domain.models.Movie;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class MovieParser {

    private final Gson gson = new Gson();

    public Movie parse(String json) {
        try {
            MovieJson mj = gson.fromJson(json, MovieJson.class);
            if (mj == null || mj.title == null || mj.title.isBlank()) {
                return null;
            }
            return new Movie(
                    mj.title,
                    parseDate(mj.releaseDate),
                    mj.voteAverage,
                    mj.genres != null ? mj.genres : List.of()
            );
        } catch (Exception e) {
            System.err.println("Error parsing movie event: " + e.getMessage());
            return null;
        }
    }

    // Una fecha ausente o mal formada no invalida la película (TMDB a veces no la envía)
    private static LocalDate parseDate(String value) {
        if (value == null || value.isBlank()) return null;
        try {
            return LocalDate.parse(value);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private static class MovieJson {
        String title;
        String releaseDate;
        double voteAverage;
        List<String> genres;
    }
}
