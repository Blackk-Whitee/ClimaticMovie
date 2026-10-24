package org.ulpgc.climaticmovie.feederweather.application.controller;

import org.ulpgc.climaticmovie.feederweather.domain.models.Weather;
import org.ulpgc.climaticmovie.feederweather.infrastructure.api.WeatherProvider;
import org.ulpgc.climaticmovie.feederweather.infrastructure.messaging.ActiveMQEventStorer;

public class Control {
    private final WeatherProvider apiClient;
    private final ActiveMQEventStorer eventPublisher;
    private final String dataSource;

    public Control(WeatherProvider apiClient, ActiveMQEventStorer eventPublisher, String dataSource) {
        this.apiClient = apiClient;
        this.eventPublisher = eventPublisher;
        this.dataSource = dataSource;
    }

    public void fetchAndSaveWeather(String city) {
        try {
            Weather weather = apiClient.fetchWeather(city);
            eventPublisher.publishWeatherEvent(weather, dataSource);
        } catch (Exception e) {
            System.err.println("Error en " + city + ": " + e.getMessage());
        }
    }
}
