package org.example.domain.services;

import com.google.gson.Gson;
import org.example.domain.models.Movie;

import java.time.LocalDate;
import java.util.List;

public class MovieParser {

    private final Gson gson = new Gson();

    public Movie parse(String json) {
        try {
            MovieJson mj = gson.fromJson(json, MovieJson.class);
            return new Movie(
                    mj.title,
                    LocalDate.parse(mj.releaseDate),
                    mj.voteAverage,
                    mj.genres
            );
        } catch (Exception e) {
            System.err.println("Error parsing movie event: " + e.getMessage());
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
