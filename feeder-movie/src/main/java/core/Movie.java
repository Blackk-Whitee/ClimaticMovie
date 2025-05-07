package core;

import java.time.Instant;
import java.util.List;

public class Movie {
    private String timestamp;
    private String ss;
    private final String title, releaseDate;
    private final double voteAverage;
    private final List<String> genres;

    public Movie(String timestamp, String ss, String title, String releaseDate, double voteAverage, List<String> genres) {
        this.timestamp = timestamp;
        this.ss = ss;
        this.genres = genres;
        this.title = title;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "Instant=" + timestamp +
                ", ss=" + ss +
                ", title='" + title + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", voteAverage=" + voteAverage +
                ", genres=" + genres +
                '}';
    }

    public String getTitle() { return title; }
    public String getReleaseDate() { return releaseDate; }
    public double getVoteAverage() { return voteAverage; }
    public List<String> getGenreIds() { return genres; }
}
