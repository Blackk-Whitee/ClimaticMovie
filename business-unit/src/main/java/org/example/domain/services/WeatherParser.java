package org.example.domain.services;

import com.google.gson.Gson;
import org.example.domain.models.Weather;

import java.time.Instant;

public class WeatherParser {

    private final Gson gson = new Gson();

    public Weather parse(String json) {
        try {
            WeatherJson wj = gson.fromJson(json, WeatherJson.class);
            return new Weather(
                    wj.city,
                    wj.temperature,
                    wj.humidity,
                    wj.condition,
                    Instant.parse(wj.ts)
            );
        } catch (Exception e) {
            System.err.println("Error parsing weather event: " + e.getMessage());
            return null;
        }
    }

    private static class WeatherJson {
        String city;
        double temperature;
        int humidity;
        String condition;
        String ts;
    }
}
