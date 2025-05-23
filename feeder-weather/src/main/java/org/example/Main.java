package org.example;

import org.example.application.controller.Control;
import org.example.infrastructure.api.WeatherProvider;
import org.example.application.scheduler.WeatherScheduler;
import org.example.infrastructure.messaging.ActiveMQEventStorer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Uso: java -jar feeder-weather.jar <api_key> <database_url> <broker_url> <cities_path>");
            System.exit(1);
        }

        String apiKey = args[0];
        String brokerUrl = args[2];
        String path = args[3];
        String[] cities = LeerCSVJava(path);
        tryCatch(apiKey, cities, brokerUrl);
    }

    private static void tryCatch(String apiKey, String[] cities, String brokerUrl) {
        try {
            WeatherProvider apiClient = new WeatherProvider("https://api.weatherapi.com", apiKey);
            ActiveMQEventStorer eventPublisher = new ActiveMQEventStorer(brokerUrl, "Weather");
            Control controller = new Control(apiClient, eventPublisher, "feeder-weather");
            WeatherScheduler scheduler = new WeatherScheduler(cities, controller);
            scheduler.start();

            Thread.currentThread().join();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static String[] LeerCSVJava(String path) {
        List<String> lineas = null;
        try {
            lineas = Files.readAllLines(Paths.get(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String[] result = lineas.get(0).replaceAll("\"", "").split(",");
        return result;
    }
}