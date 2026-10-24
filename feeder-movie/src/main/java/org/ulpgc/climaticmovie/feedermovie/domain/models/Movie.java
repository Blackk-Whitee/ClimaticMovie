package org.ulpgc.climaticmovie.feedermovie.domain.models;

import java.util.List;

public class Movie {
    private final String topic;
    private final String ts;
    private final String ss;
    private final String title;
    private final String releaseDate;
    private final double voteAverage;
    private final List<String> genres;

    public Movie(String topic, String ts, String ss, String title, String releaseDate, double voteAverage, List<String> genres) {
        this.topic = topic;
        this.ts = ts;
        this.ss = ss;
        this.title = title;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
        this.genres = genres;
    }

    @Override
    public String toString() {
        return "Movie{title='" + title + "', releaseDate='" + releaseDate + "', voteAverage=" + voteAverage
                + ", genres=" + genres + '}';
    }

    public String getTopic() { return topic; }
    public String getTs() { return ts; }
    public String getSs() { return ss; }
    public String getTitle() { return title; }
    public String getReleaseDate() { return releaseDate; }
    public double getVoteAverage() { return voteAverage; }
    public List<String> getGenres() { return genres; }
}
