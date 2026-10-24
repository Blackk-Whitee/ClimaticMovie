package org.ulpgc.climaticmovie.feederweather.domain.models;

import java.time.Instant;

public class Weather {
    private final String city;
    private final double temperature;
    private final int humidity;
    private final String condition;
    private final double windSpeed;
    private final Instant timestamp;

    public Weather(String city, double temperature, int humidity, String condition, double windSpeed, Instant timestamp) {
        this.city = city;
        this.temperature = temperature;
        this.humidity = humidity;
        this.condition = condition;
        this.windSpeed = windSpeed;
        this.timestamp = timestamp;
    }

    public String getCity() { return city; }
    public double getTemperature() { return temperature; }
    public int getHumidity() { return humidity; }
    public String getCondition() { return condition; }
    public double getWindSpeed() { return windSpeed; }
    public Instant getTimestamp() { return timestamp; }
}
