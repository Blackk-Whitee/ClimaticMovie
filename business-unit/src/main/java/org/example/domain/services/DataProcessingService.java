package org.example.domain.services;

import org.example.domain.models.*;
import org.example.infrastructure.store.SQLiteDatamart;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.ArrayList;

public class DataProcessingService {

    private final WeatherParser weatherParser;
    private final MovieParser movieParser;
    private final DatamartUpdater datamartUpdater;

    public DataProcessingService(SQLiteDatamart datamart, RecommendationService recommendationService) {
        this.weatherParser = new WeatherParser();
        this.movieParser = new MovieParser();
        this.datamartUpdater = new DatamartUpdater(datamart, recommendationService);
    }

    public void processHistoricalData(List<String> weatherEvents, List<String> movieEvents) {
        List<Weather> weatherData = weatherEvents.stream()
                .map(weatherParser::parse)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        List<Movie> movieData = movieEvents.stream()
                .map(movieParser::parse)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        datamartUpdater.updateDatamart(weatherData, movieData);
    }

}
