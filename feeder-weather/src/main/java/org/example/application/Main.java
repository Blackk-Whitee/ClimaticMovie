package org.example.application;

import org.example.application.controller.WeatherController;
import org.example.infrastructure.api.WeatherApiClient;
import org.example.infrastructure.database.WeatherDatabase;
import org.example.application.scheduler.WeatherScheduler;

public class Main {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Uso: java -jar feeder-weather.jar <api_key> <db_url>");
            System.exit(1);
        }

        String apiKey = args[0];
        String dbUrl = args[1];
        String[] cities = {
                "Albacete", "Alicante", "Almería", "Ávila", "Badajoz", "Barcelona", "Bilbao", "Burgos",
                "Cáceres", "Cádiz", "Castellón de la Plana", "Ciudad Real", "Córdoba", "Cuenca", "Girona",
                "Granada", "Guadalajara", "Huelva", "Huesca", "Jaén", "La Coruña", "Logroño", "Las Palmas de Gran Canaria",
                "León", "Lleida", "Lugo", "Madrid", "Málaga", "Murcia", "Orense", "Oviedo", "Palencia",
                "Palma", "Pamplona", "Pontevedra", "Salamanca", "San Sebastián", "Santa Cruz de Tenerife",
                "Santander", "Segovia", "Sevilla", "Soria", "Tarragona", "Teruel", "Toledo", "Valencia",
                "Valladolid", "Vitoria-Gasteiz", "Zamora", "Zaragoza", "Ceuta", "Melilla"
        };

        tryCatch(apiKey, dbUrl, cities);
    }

    private static void tryCatch(String apiKey, String dbUrl, String[] cities) {
        try {
            WeatherApiClient apiClient = new WeatherApiClient("https://api.weatherapi.com", apiKey);
            WeatherDatabase database = new WeatherDatabase(dbUrl);

            WeatherController controller = new WeatherController(apiClient, database);

            WeatherScheduler scheduler = new WeatherScheduler(cities, controller);
            scheduler.start();

            Thread.currentThread().join();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}