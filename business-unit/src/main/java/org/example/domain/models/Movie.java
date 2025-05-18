package org.example.domain.models;

import java.time.LocalDate;
import java.util.List;

public class Movie {
    private final String title;
    private final LocalDate releaseDate;
    private final double voteAverage;
    private final List<String> genres;

    public Movie(String title, LocalDate releaseDate, double voteAverage, List<String> genres) {
        this.title = title;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
        this.genres = genres;
    }

    // Getters
    public String getTitle() { return title; }
    public LocalDate getReleaseDate() { return releaseDate; }
    public double getVoteAverage() { return voteAverage; }
    public List<String> getGenres() { return genres; }
}