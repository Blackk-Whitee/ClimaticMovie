package org.ulpgc.climaticmovie.feederweather;

import org.ulpgc.climaticmovie.feederweather.application.controller.Control;
import org.ulpgc.climaticmovie.feederweather.application.scheduler.WeatherScheduler;
import org.ulpgc.climaticmovie.feederweather.infrastructure.api.WeatherProvider;
import org.ulpgc.climaticmovie.feederweather.infrastructure.messaging.ActiveMQEventStorer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class Main {
    private static final String API_KEY_ENV = "WEATHER_API_KEY";
    private static final String API_BASE_URL = "https://api.weatherapi.com";
    private static final String TOPIC = "Weather";
    private static final String SOURCE = "feeder-weather";

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.err.println("Uso: java -jar feeder-weather.jar <broker_url> <cities_file>");
            System.err.println("La API key se lee de la variable de entorno " + API_KEY_ENV);
            System.exit(1);
        }
        String apiKey = System.getenv(API_KEY_ENV);
        if (apiKey == null || apiKey.isBlank()) {
            System.err.println("Falta la variable de entorno " + API_KEY_ENV);
            System.exit(1);
        }
        List<String> cities = readCities(Path.of(args[1]));

        ActiveMQEventStorer eventPublisher = new ActiveMQEventStorer(args[0], TOPIC);
        Runtime.getRuntime().addShutdownHook(new Thread(eventPublisher::close));
        Control controller = new Control(new WeatherProvider(API_BASE_URL, apiKey), eventPublisher, SOURCE);
        new WeatherScheduler(cities, controller).start();
        System.out.println("feeder-weather iniciado para " + cities.size() + " ciudades (cada hora).");
        Thread.currentThread().join();
    }

    static List<String> readCities(Path path) throws IOException {
        return Files.readAllLines(path).stream()
                .flatMap(line -> Arrays.stream(line.split(",")))
                .map(city -> city.replace("\"", "").trim())
                .filter(city -> !city.isEmpty())
                .toList();
    }
}
