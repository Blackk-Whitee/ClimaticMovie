package org.example.application.controller;

import org.example.domain.models.Weather;
import org.example.infrastructure.api.WeatherApiClient;
import org.example.infrastructure.database.WeatherDatabase;

public class WeatherController {
    private final WeatherApiClient apiClient;
    private final WeatherDatabase database;

    public WeatherController(WeatherApiClient apiClient, WeatherDatabase database) {
        this.apiClient = apiClient;
        this.database = database;
    }

    public void fetchAndSaveWeather(String city) {
        try {
            Weather weather = apiClient.fetchWeather(city);
            database.saveWeather(weather);
            System.out.println("Datos procesados para: " + city);
        } catch (Exception e) {
            System.err.println("Error en " + city + ": " + e.getMessage());
        }
    }
}
