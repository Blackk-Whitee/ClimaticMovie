package org.example.domain.models;

import com.google.gson.annotations.SerializedName;
import java.time.LocalDateTime;
import java.util.Locale;

public class Weather {
    private String topic;
    private String city;
    private double temperature;
    private int humidity;

    @SerializedName("condition_text")
    private String condition;

    @SerializedName("wind_kph")
    private double windSpeed;

    private LocalDateTime timestamp;

    public Weather(String topic, String name, double tempC, int humidity, String text, double windKph, LocalDateTime now) {
        this.topic = topic;
        this.city = name;
        this.temperature = tempC;
        this.humidity = humidity;
        this.condition = text;
        this.windSpeed = windKph;
        this.timestamp = now;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public double getTemperature() {
        return temperature;
    }

    public String getTemperatureAsString() {
        return String.format(Locale.US, "%.2f", this.temperature);
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getTopic() {return topic;}

    public void setTopic(String topic) {this.topic = topic;}
}