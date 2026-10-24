package org.ulpgc.climaticmovie.businessunit.domain.models;

import java.util.*;

public class WeatherCondition {
    private static final List<String> SUNNY_GENRES = List.of("Aventura", "Comedia", "Familia", "Fantasía", "Animación", "Música");
    private static final List<String> RAINY_GENRES = List.of("Drama", "Romance", "Misterio", "Ciencia ficción", "Documental");
    private static final List<String> CLOUDY_GENRES = List.of("Suspense", "Misterio", "Crimen", "Ciencia ficción", "Historia");
    private static final List<String> SNOWY_GENRES = List.of("Fantasía", "Drama", "Animación", "Historia", "Bélica", "Película de TV");
    private static final List<String> EXTREME_GENRES = List.of("Terror", "Suspense", "Crimen", "Ciencia ficción", "Documental");
    private static final List<String> DEFAULT_GENRES = List.of("Drama", "Comedia", "Aventura");

    // Textos de condición devueltos por WeatherAPI.com; la búsqueda no distingue mayúsculas
    private static final Map<String, List<String>> WEATHER_TO_GENRE_MAP = createWeatherGenreMap();

    private static Map<String, List<String>> createWeatherGenreMap() {
        Map<String, List<String>> map = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        register(map, SUNNY_GENRES, "Sunny", "Clear", "Partly cloudy");
        register(map, RAINY_GENRES, "Light rain", "Light drizzle", "Light rain shower", "Patchy light drizzle",
                "Patchy light rain", "Heavy rain", "Heavy rain at times", "Torrential rain shower", "Moderate rain",
                "Moderate rain at times", "Moderate or heavy rain shower", "Moderate or heavy rain with thunder",
                "Patchy rain possible", "Patchy rain nearby");
        register(map, CLOUDY_GENRES, "Cloudy", "Overcast", "Mist", "Fog", "Thundery outbreaks possible",
                "Thundery outbreaks in nearby", "Patchy light rain with thunder");
        register(map, SNOWY_GENRES, "Light snow", "Light snow showers", "Moderate snow", "Patchy moderate snow",
                "Patchy snow possible", "Patchy snow nearby", "Patchy light snow", "Patchy heavy snow", "Heavy snow",
                "Moderate or heavy snow showers", "Moderate or heavy snow with thunder");
        register(map, EXTREME_GENRES, "Blizzard", "Blowing snow", "Freezing drizzle", "Heavy freezing drizzle",
                "Light freezing rain", "Moderate or heavy freezing rain", "Ice pellets", "Light showers of ice pellets",
                "Moderate or heavy showers of ice pellets", "Light sleet", "Moderate or heavy sleet",
                "Light sleet showers", "Moderate or heavy sleet showers", "Patchy sleet possible",
                "Patchy sleet nearby", "Freezing fog", "Patchy freezing drizzle possible",
                "Patchy freezing drizzle nearby");
        return Collections.unmodifiableMap(map);
    }

    private static void register(Map<String, List<String>> map, List<String> genres, String... conditions) {
        for (String condition : conditions) {
            map.put(condition, genres);
        }
    }

    public static List<String> getGenresForWeather(String weatherCondition) {
        if (weatherCondition == null) {
            return DEFAULT_GENRES;
        }
        return WEATHER_TO_GENRE_MAP.getOrDefault(weatherCondition.trim(), DEFAULT_GENRES);
    }
}
