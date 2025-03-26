package org.example;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.google.gson.Gson;

public class WeatherAPIFeeder {
    private static final String API_KEY = "80d374e907784355b7f144840251903";
    private static final String BASE_URL = "http://api.weatherapi.com/v1/current.json";

    public static void main(String[] args) {
        String location = "Madrid"; // Puedes cambiarlo por una ciudad o "lat,long"
        fetchWeatherData(location);
    }

    public static void fetchWeatherData(String location) {
        HttpClient client = HttpClient.newHttpClient();
        String url = BASE_URL + "?key=" + API_KEY + "&q=" + location + "&aqi=no";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                String jsonResponse = response.body();
                parseWeatherData(jsonResponse);
            } else {
                System.err.println("Error: " + response.statusCode() + " - " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void parseWeatherData(String json) {
        Gson gson = new Gson();
        WeatherData weatherData = gson.fromJson(json, WeatherData.class);

        System.out.println("🌤️ Clima en " + weatherData.location.name + ", " + weatherData.location.country);
        System.out.println("🌡️ Temperatura: " + weatherData.current.temp_c + "°C");
        System.out.println("💧 Humedad: " + weatherData.current.humidity + "%");
        System.out.println("🌬️ Viento: " + weatherData.current.wind_kph + " km/h");
    }
}

// Clases para mapear la respuesta JSON
class WeatherData {
    Location location;
    Current current;
}

class Location {
    String name;
    String country;
}

class Current {
    double temp_c;
    int humidity;
    double wind_kph;
}