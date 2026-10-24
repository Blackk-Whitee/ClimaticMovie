package org.ulpgc.climaticmovie.feederweather.infrastructure.api;

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

        static class Current {
            @SerializedName("temp_c") double tempC;
            Condition condition;
            int humidity;
            @SerializedName("wind_kph") double windKph;
        }

        static class Condition {
            String text;
        }

        static class Location {
            String name;
        }
    }
}
