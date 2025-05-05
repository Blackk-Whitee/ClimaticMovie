package org.example.application.controller;

import org.example.domain.models.Weather;
import org.example.infrastructure.api.WeatherProvider;
// import org.example.infrastructure.database.SQLiteEventStorer;
import org.example.infrastructure.messaging.ActiveMQEventStorer;

public class Control {
    private final WeatherProvider apiClient;
    private final ActiveMQEventStorer eventPublisher;
    private final String dataSource;
    // private final SQLiteEventStorer database;

    public Control(WeatherProvider apiClient, ActiveMQEventStorer eventPublisher, String dataSource) {
        this.apiClient = apiClient;
        this.eventPublisher = eventPublisher;
        this.dataSource = dataSource;
        // this.database = database
    }

    public void fetchAndSaveWeather(String city) {
        try {
            Weather weather = apiClient.fetchWeather(city);
            eventPublisher.publishWeatherEvent(weather, dataSource);
            // database.saveWeather(weather)
        } catch (Exception e) {
            System.err.println("Error en " + city + ": " + e.getMessage());
        }
    }
}
