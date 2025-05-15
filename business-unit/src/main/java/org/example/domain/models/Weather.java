package org.example.domain.models;

import java.time.Instant;

public class Weather {
    private final String city;
    private final double temperature;
    private final int humidity;
    private final String condition;
    private final Instant timestamp;

    public Weather(String city, double temperature, int humidity, String condition, Instant timestamp) {
        this.city = city;
        this.temperature = temperature;
        this.humidity = humidity;
        this.condition = condition;
        this.timestamp = timestamp;
    }

    // Getters
    public String getCity() { return city; }
    public double getTemperature() { return temperature; }
    public int getHumidity() { return humidity; }
    public String getCondition() { return condition; }
    public Instant getTimestamp() { return timestamp; }
}