package org.example.domain.services;

import org.example.domain.models.*;
import org.example.infrastructure.store.SQLiteDatamart;

import java.util.List;

public class DatamartUpdater {

    private final SQLiteDatamart datamart;
    private final RecommendationService recommendationService;

    public DatamartUpdater(SQLiteDatamart datamart, RecommendationService recommendationService) {
        this.datamart = datamart;
        this.recommendationService = recommendationService;
    }

    public SQLiteDatamart getDatamart() {
        return datamart;
    }

    public void updateDatamart(List<Weather> weatherData, List<Movie> movieData) {
        if (!weatherData.isEmpty()) {
            datamart.updateWeatherData(weatherData);
        }
        if (!movieData.isEmpty()) {
            datamart.updateMovies(movieData);
        }

        if (!weatherData.isEmpty() || !movieData.isEmpty()) {
            List<Recommendation> recommendations = recommendationService.generateRecommendations(weatherData, movieData);
            datamart.updateRecommendations(recommendations);
        }
    }
}
