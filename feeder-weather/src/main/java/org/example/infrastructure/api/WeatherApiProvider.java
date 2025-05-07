package org.example.infrastructure.api;

import com.google.gson.annotations.SerializedName;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApiProvider {
    @GET("/v1/current.json")
    Call<WeatherApiResponse> getCurrentWeather(
            @Query("key") String apiKey,
            @Query("q") String city,
            @Query("aqi") String aqi
    );

    class WeatherApiResponse {
        Current current;
        Location location;

        class Current {
            @SerializedName("temp_c") double tempC;
            @SerializedName("condition") Condition condition;
            int humidity;
            @SerializedName("wind_kph") double windKph;
        }

        class Condition {
            String text;
        }

        class Location {
            String name;
        }
    }
}
