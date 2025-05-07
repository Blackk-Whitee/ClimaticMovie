package org.example.infrastructure.api;

import org.example.domain.models.Weather;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.time.LocalDateTime;

public class WeatherProvider {
    private final WeatherApiProvider service;
    private final String apiKey;

    public WeatherProvider(String baseUrl, String apiKey) {
        this.apiKey = apiKey;
        this.service = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WeatherApiProvider.class);
    }

    public Weather fetchWeather(String city) throws Exception {
        var response = service.getCurrentWeather(apiKey, city, "no").execute();
        if (!response.isSuccessful()) throw new Exception("API Error: " + response.code());

        var apiResponse = response.body();
        return new Weather(apiResponse.location.name, apiResponse.current.tempC, apiResponse.current.humidity, apiResponse.current.condition.text, apiResponse.current.windKph, LocalDateTime.now());
    }
}