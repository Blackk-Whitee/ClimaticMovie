package org.example.domain.models;

import java.util.List;

public class Recommendation {
    private final String city;
    private final String weatherCondition;
    private final List<String> recommendedMovies;

    public Recommendation(String city, String weatherCondition, List<String> recommendedMovies) {
        this.city = city;
        this.weatherCondition = weatherCondition;
        this.recommendedMovies = recommendedMovies;
    }

    // Getters
    public String getCity() { return city; }
    public String getWeatherCondition() { return weatherCondition; }
    public List<String> getRecommendedMovies() { return recommendedMovies; }
}