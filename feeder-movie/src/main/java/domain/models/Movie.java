package domain.models;

import java.time.Instant;
import java.util.List;

public class Movie {
    private String topic;
    private String ts;
    private String ss;
    private final String title, releaseDate;
    private final double voteAverage;
    private final List<String> genres;

    public Movie(String topic, String ts, String ss, String title, String releaseDate, double voteAverage, List<String> genres) {
        this.topic = topic;
        this.ts = ts;
        this.ss = ss;
        this.genres = genres;
        this.title = title;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "topic='" + topic + '\'' +
                ", Instant=" + ts +
                ", ss=" + ss +
                ", title='" + title + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", voteAverage=" + voteAverage +
                ", genres=" + genres +
                '}';
    }

    public String getTopic() { return topic; }
    public String getTitle() { return title; }
    public String getReleaseDate() { return releaseDate; }
    public double getVoteAverage() { return voteAverage; }
    public List<String> getGenreIds() { return genres; }
    public String getSs() { return ss; }
    public Instant getTs() { return Instant.parse(ts); }
}
