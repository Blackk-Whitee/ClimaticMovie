package core;

import java.util.List;

public class Movie {
    private final int id;
    private final String title, releaseDate;
    private final double voteAverage;
    private final int voteCount;
    private final List<Integer> genreIds;

    public Movie(int id, String title, String releaseDate,
                 double voteAverage, int voteCount, List<Integer> genreIds) {
        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
        this.voteCount = voteCount;
        this.genreIds = genreIds;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getReleaseDate() { return releaseDate; }
    public double getVoteAverage() { return voteAverage; }
    public int getVoteCount() { return voteCount; }
    public List<Integer> getGenreIds() { return genreIds; }
}
