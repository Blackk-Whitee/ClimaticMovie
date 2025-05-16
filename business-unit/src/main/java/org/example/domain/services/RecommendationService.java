package org.example.domain.services;

import org.example.domain.models.*;
import java.util.*;
import java.util.stream.Collectors;

public class RecommendationService {
    public List<Recommendation> generateRecommendations(List<Weather> weatherData, List<Movie> movies) {
        return weatherData.stream()
                .map(weather -> createRecommendationForCity(weather, movies))
                .collect(Collectors.toList());
    }

    private Recommendation createRecommendationForCity(Weather weather, List<Movie> allMovies) {
        List<String> suitableGenres = WeatherCondition.getGenresForWeather(weather.getCondition());

        List<String> recommendedMovies = allMovies.stream()
                .filter(movie -> hasMatchingGenre(movie, suitableGenres))
                .sorted(Comparator.comparingDouble(Movie::getVoteAverage).reversed())
                .map(Movie::getTitle)
                .collect(Collectors.toList());

        return new Recommendation(weather.getCity(), weather.getCondition(), recommendedMovies);
    }

    private boolean hasMatchingGenre(Movie movie, List<String> genresToMatch) {
        return movie.getGenres().stream()
                .anyMatch(genre -> genresToMatch.stream()
                        .anyMatch(g -> g.equalsIgnoreCase(genre)));
    }
}