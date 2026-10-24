package org.ulpgc.climaticmovie.feedermovie.infrastructure.api;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ulpgc.climaticmovie.feedermovie.domain.models.Movie;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class TmdbMovieProvider implements MovieProvider {
    private static final String BASE_URL = "https://api.themoviedb.org/3/trending/movie/day?language=es-ES";
    private static final String GENRE_LIST_URL = "https://api.themoviedb.org/3/genre/movie/list?language=es-ES";
    private static final String TOPIC = "movies.Trending";
    private static final String SOURCE = "feeder movie";
    private static final int MAX_PAGES = 5;

    private final String apiKey;
    private final HttpClient http = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();
    private Map<Integer, String> genres = Map.of();

    public TmdbMovieProvider(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public List<Movie> provide() {
        if (genres.isEmpty()) {
            genres = fetchGenres();
        }
        List<Movie> allMovies = new ArrayList<>();
        for (int page = 1; page <= MAX_PAGES; page++) {
            try {
                allMovies.addAll(parseMovies(get(BASE_URL + "&page=" + page), genres));
            } catch (Exception e) {
                System.err.println("Error obteniendo la página " + page + " de TMDB: " + e.getMessage());
            }
        }
        return allMovies;
    }

    static List<Movie> parseMovies(String json, Map<Integer, String> genres) {
        JSONArray results = new JSONObject(json).getJSONArray("results");
        String ts = Instant.now().toString();
        List<Movie> movies = new ArrayList<>();
        for (int i = 0; i < results.length(); i++) {
            JSONObject obj = results.getJSONObject(i);
            String title = obj.optString("title", "");
            if (title.isBlank()) continue;
            movies.add(new Movie(TOPIC, ts, SOURCE, title,
                    obj.optString("release_date", ""),
                    obj.optDouble("vote_average", 0.0),
                    genreNames(obj.optJSONArray("genre_ids"), genres)));
        }
        return movies;
    }

    private static List<String> genreNames(JSONArray ids, Map<Integer, String> genres) {
        List<String> names = new ArrayList<>();
        if (ids == null) return names;
        for (int i = 0; i < ids.length(); i++) {
            String name = genres.get(ids.optInt(i));
            if (name != null) names.add(name);
        }
        return names;
    }

    private Map<Integer, String> fetchGenres() {
        try {
            Map<Integer, String> result = new HashMap<>();
            new JSONObject(get(GENRE_LIST_URL)).getJSONArray("genres").forEach(g ->
                    result.put(((JSONObject) g).getInt("id"), ((JSONObject) g).getString("name")));
            return result;
        } catch (Exception e) {
            System.err.println("Error obteniendo los géneros de TMDB: " + e.getMessage());
            return Map.of();
        }
    }

    private String get(String url) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder(URI.create(url + "&api_key=" + apiKey))
                .timeout(Duration.ofSeconds(20))
                .GET()
                .build();
        HttpResponse<String> response = http.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new IOException("HTTP " + response.statusCode());
        }
        return response.body();
    }
}
