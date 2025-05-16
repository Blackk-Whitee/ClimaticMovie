package org.example.domain.models;

import java.util.*;

public class WeatherCondition {
    private static final Map<String, List<String>> WEATHER_TO_GENRE_MAP = createWeatherGenreMap();

    private static Map<String, List<String>> createWeatherGenreMap() {
        Map<String, List<String>> map = new HashMap<>();

// Condiciones soleadas/despejadas
        map.put("Sunny", Arrays.asList("Aventura", "Comedia", "Familia", "Fantasía", "Animación", "Música"));
        map.put("Clear", Arrays.asList("Aventura", "Comedia", "Familia", "Fantasía", "Animación", "Música"));
        map.put("Partly cloudy", Arrays.asList("Aventura", "Comedia", "Familia", "Fantasía", "Animación", "Música"));

// Condiciones lluviosas
        map.put("Light rain", Arrays.asList("Drama", "Romance", "Misterio", "Ciencia ficción", "Documental"));
        map.put("Light drizzle", Arrays.asList("Drama", "Romance", "Misterio", "Ciencia ficción", "Documental"));
        map.put("Light rain shower", Arrays.asList("Drama", "Romance", "Misterio", "Ciencia ficción", "Documental"));
        map.put("Patchy light drizzle", Arrays.asList("Drama", "Romance", "Misterio", "Ciencia ficción", "Documental"));
        map.put("Patchy light rain", Arrays.asList("Drama", "Romance", "Misterio", "Ciencia ficción", "Documental"));
        map.put("Heavy rain", Arrays.asList("Drama", "Romance", "Misterio", "Ciencia ficción", "Documental"));
        map.put("Heavy rain at times", Arrays.asList("Drama", "Romance", "Misterio", "Ciencia ficción", "Documental"));
        map.put("Torrential rain shower", Arrays.asList("Drama", "Romance", "Misterio", "Ciencia ficción", "Documental"));
        map.put("Moderate rain", Arrays.asList("Drama", "Romance", "Misterio", "Ciencia ficción", "Documental"));
        map.put("Moderate rain at times", Arrays.asList("Drama", "Romance", "Misterio", "Ciencia ficción", "Documental"));
        map.put("Moderate or heavy rain shower", Arrays.asList("Drama", "Romance", "Misterio", "Ciencia ficción", "Documental"));
        map.put("Moderate or heavy rain with thunder", Arrays.asList("Drama", "Romance", "Misterio", "Ciencia ficción", "Documental"));
        map.put("Patchy rain possible", Arrays.asList("Drama", "Romance", "Misterio", "Ciencia ficción", "Documental"));

// Condiciones nubladas
        map.put("Cloudy", Arrays.asList("Suspense", "Misterio", "Crimen", "Ciencia ficción", "Historia"));
        map.put("Overcast", Arrays.asList("Suspense", "Misterio", "Crimen", "Ciencia ficción", "Historia"));
        map.put("Mist", Arrays.asList("Suspense", "Misterio", "Crimen", "Ciencia ficción", "Historia"));
        map.put("Fog", Arrays.asList("Suspense", "Misterio", "Crimen", "Ciencia ficción", "Historia"));
        map.put("Thundery outbreaks possible", Arrays.asList("Suspense", "Misterio", "Crimen", "Ciencia ficción", "Historia"));
        map.put("Patchy light rain with thunder", Arrays.asList("Suspense", "Misterio", "Crimen", "Ciencia ficción", "Historia"));

// Condiciones de nieve
        map.put("Light snow", Arrays.asList("Fantasía", "Drama", "Animación", "Historia", "Bélica", "Película de TV"));
        map.put("Light snow showers", Arrays.asList("Fantasía", "Drama", "Animación", "Historia", "Bélica", "Película de TV"));
        map.put("Moderate snow", Arrays.asList("Fantasía", "Drama", "Animación", "Historia", "Bélica", "Película de TV"));
        map.put("Patchy moderate snow", Arrays.asList("Fantasía", "Drama", "Animación", "Historia", "Bélica", "Película de TV"));
        map.put("Patchy snow possible", Arrays.asList("Fantasía", "Drama", "Animación", "Historia", "Bélica", "Película de TV"));
        map.put("Patchy light snow", Arrays.asList("Fantasía", "Drama", "Animación", "Historia", "Bélica", "Película de TV"));
        map.put("Patchy heavy snow", Arrays.asList("Fantasía", "Drama", "Animación", "Historia", "Bélica", "Película de TV"));
        map.put("Heavy snow", Arrays.asList("Fantasía", "Drama", "Animación", "Historia", "Bélica", "Película de TV"));
        map.put("Moderate or heavy snow showers", Arrays.asList("Fantasía", "Drama", "Animación", "Historia", "Bélica", "Película de TV"));
        map.put("Moderate or heavy snow with thunder", Arrays.asList("Fantasía", "Drama", "Animación", "Historia", "Bélica", "Película de TV"));

// Condiciones extremas
        map.put("Blizzard", Arrays.asList("Terror", "Suspense", "Crimen", "Ciencia ficción", "Documental"));
        map.put("Blowing snow", Arrays.asList("Terror", "Suspense", "Crimen", "Ciencia ficción", "Documental"));
        map.put("Freezing drizzle", Arrays.asList("Terror", "Suspense", "Crimen", "Ciencia ficción", "Documental"));
        map.put("Heavy freezing drizzle", Arrays.asList("Terror", "Suspense", "Crimen", "Ciencia ficción", "Documental"));
        map.put("Light freezing rain", Arrays.asList("Terror", "Suspense", "Crimen", "Ciencia ficción", "Documental"));
        map.put("Moderate or heavy freezing rain", Arrays.asList("Terror", "Suspense", "Crimen", "Ciencia ficción", "Documental"));
        map.put("Ice pellets", Arrays.asList("Terror", "Suspense", "Crimen", "Ciencia ficción", "Documental"));
        map.put("Light showers of ice pellets", Arrays.asList("Terror", "Suspense", "Crimen", "Ciencia ficción", "Documental"));
        map.put("Moderate or heavy showers of ice pellets", Arrays.asList("Terror", "Suspense", "Crimen", "Ciencia ficción", "Documental"));
        map.put("Light sleet", Arrays.asList("Terror", "Suspense", "Crimen", "Ciencia ficción", "Documental"));
        map.put("Moderate or heavy sleet", Arrays.asList("Terror", "Suspense", "Crimen", "Ciencia ficción", "Documental"));
        map.put("Light sleet showers", Arrays.asList("Terror", "Suspense", "Crimen", "Ciencia ficción", "Documental"));
        map.put("Moderate or heavy sleet showers", Arrays.asList("Terror", "Suspense", "Crimen", "Ciencia ficción", "Documental"));
        map.put("Patchy sleet possible", Arrays.asList("Terror", "Suspense", "Crimen", "Ciencia ficción", "Documental"));
        map.put("Freezing fog", Arrays.asList("Terror", "Suspense", "Crimen", "Ciencia ficción", "Documental"));
        map.put("Patchy freezing drizzle possible", Arrays.asList("Terror", "Suspense", "Crimen", "Ciencia ficción", "Documental"));

        // Condición por defecto
        map.put("default", Arrays.asList("Drama", "Comedia", "Aventura"));
        return map;
    }
    public static List<String> getGenresForWeather(String weatherCondition) {
        return WEATHER_TO_GENRE_MAP.getOrDefault(
                weatherCondition,
                WEATHER_TO_GENRE_MAP.get("default")
        );
    }
}