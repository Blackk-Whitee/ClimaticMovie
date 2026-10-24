package org.ulpgc.climaticmovie.feederweather.infrastructure.api;

import org.ulpgc.climaticmovie.feederweather.domain.models.Weather;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.time.Instant;

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

    public Weather fetchWeather(String city) throws IOException {
        var response = service.getCurrentWeather(apiKey, city, "no").execute();
        var body = response.body();
        if (!response.isSuccessful() || body == null || body.current == null || body.location == null) {
            throw new IOException("API Error: HTTP " + response.code());
        }
        String condition = body.current.condition != null ? body.current.condition.text : null;
        return new Weather(body.location.name, body.current.tempC, body.current.humidity,
                condition, body.current.windKph, Instant.now());
    }
}
