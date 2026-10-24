package org.ulpgc.climaticmovie.businessunit.domain.services;

import org.ulpgc.climaticmovie.businessunit.domain.models.*;
import org.ulpgc.climaticmovie.businessunit.infrastructure.store.SQLiteDatamart;

import java.util.List;

public class DatamartUpdater {

    private final SQLiteDatamart datamart;
    private final RecommendationService recommendationService;

    public DatamartUpdater(SQLiteDatamart datamart, RecommendationService recommendationService) {
        this.datamart = datamart;
        this.recommendationService = recommendationService;
    }

    public void updateDatamart(List<Weather> weatherData, List<Movie> movieData) {
        if (weatherData.isEmpty() && movieData.isEmpty()) {
            return;
        }
        // El clima se actualiza por ciudad, así un lote parcial no borra las demás ciudades
        if (!weatherData.isEmpty()) {
            datamart.upsertWeatherData(weatherData);
        }
        // Las películas son el trending del día: un lote nuevo sustituye al anterior
        if (!movieData.isEmpty()) {
            datamart.replaceMovies(movieData);
        }
        List<Recommendation> recommendations = recommendationService.generateRecommendations(
                datamart.getAllWeatherData(), datamart.getAllMovies());
        datamart.updateRecommendations(recommendations);
    }
}
