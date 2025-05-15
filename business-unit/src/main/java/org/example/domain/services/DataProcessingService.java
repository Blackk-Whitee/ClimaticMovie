package org.example.domain.services;

import org.example.domain.models.*;
import org.example.infrastructure.store.SQLiteDatamart;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class DataProcessingService {
    private final SQLiteDatamart datamart;
    private final RecommendationService recommendationService;
    private final Gson gson = new Gson();
    private final JsonParser jsonParser = new JsonParser();

    public DataProcessingService(SQLiteDatamart datamart, RecommendationService recommendationService) {
        this.datamart = datamart;
        this.recommendationService = recommendationService;
    }

    public void processHistoricalData(List<String> weatherEvents, List<String> movieEvents) {
        // Procesar datos históricos de clima
        List<Weather> weatherData = weatherEvents.stream()
                .map(this::parseWeatherEvent)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        // Procesar datos históricos de películas
        List<Movie> movieData = movieEvents.stream()
                .map(this::parseMovieEvent)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        // Actualizar datamart
        updateDatamart(weatherData, movieData);
    }

    public void processNewWeatherData(String jsonWeather) {
        Weather weather = parseWeatherEvent(jsonWeather);
        if (weather != null) {
            List<Weather> currentWeather = datamart.getAllWeatherData();

            // Reemplazar datos de esta ciudad
            currentWeather.removeIf(w -> w.getCity().equals(weather.getCity()));
            currentWeather.add(weather);

            // Actualizar datamart con películas existentes
            updateDatamart(currentWeather, datamart.getAllMovies());
        }
    }

    public void processNewMovieData(String jsonMovie) {
        Movie movie = parseMovieEvent(jsonMovie);
        if (movie != null) {
            List<Movie> currentMovies = datamart.getAllMovies();

            // Añadir nueva película (asumiendo que no hay duplicados)
            currentMovies.add(movie);

            // Actualizar datamart con clima existente
            updateDatamart(datamart.getAllWeatherData(), currentMovies);
        }
    }

    private void updateDatamart(List<Weather> weatherData, List<Movie> movieData) {
        // Actualizar tablas de datos crudos
        datamart.updateWeatherData(weatherData);
        datamart.updateMovies(movieData);

        // Generar y actualizar recomendaciones
        List<Recommendation> recommendations = recommendationService.generateRecommendations(weatherData, movieData);
        datamart.updateRecommendations(recommendations);
    }

    private Weather parseWeatherEvent(String json) {
        try {
            JsonObject obj = jsonParser.parse(json).getAsJsonObject();
            return new Weather(
                    obj.get("city").getAsString(),
                    obj.get("temperature").getAsDouble(),
                    obj.get("humidity").getAsInt(),
                    obj.get("condition").getAsString(),
                    Instant.parse(obj.get("ts").getAsString())
            );
        } catch (Exception e) {
            System.err.println("Error parsing weather event: " + e.getMessage());
            return null;
        }
    }

    private Movie parseMovieEvent(String json) {
        try {
            JsonObject obj = jsonParser.parse(json).getAsJsonObject();
            return new Movie(
                    obj.get("title").getAsString(),
                    LocalDate.parse(obj.get("releaseDate").getAsString()),
                    obj.get("voteAverage").getAsDouble(),
                    gson.fromJson(obj.get("genres").getAsJsonArray(), List.class)
            );
        } catch (Exception e) {
            System.err.println("Error parsing movie event: " + e.getMessage());
            return null;
        }
    }
}