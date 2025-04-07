package infrastructure.api;

import core.Movie;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MovieApiClient {
    private final String apiKey;
    private static final String BASE_URL = "https://api.themoviedb.org/3/trending/movie/day?language=es-ES";

    public MovieApiClient(String apiKey) {
        this.apiKey = apiKey;
    }

    public List<Movie> fetchTrendingMovies(int page) {
        List<Movie> movies = new ArrayList<>();
        String urlString = BASE_URL + "&api_key=" + apiKey + "&page=" + page;

        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Error en la petición: " + conn.getResponseCode());
            }

            Scanner scanner = new Scanner(url.openStream());
            StringBuilder response = new StringBuilder();
            while (scanner.hasNext()) {
                response.append(scanner.nextLine());
            }
            scanner.close();

            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONArray results = jsonResponse.getJSONArray("results");

            for (int i = 0; i < results.length(); i++) {
                JSONObject movieJson = results.getJSONObject(i);
                JSONArray genreIdsArray = movieJson.getJSONArray("genre_ids");
                List<Integer> genreIds = new ArrayList<>();
                for (int j = 0; j < genreIdsArray.length(); j++) {
                    genreIds.add(genreIdsArray.getInt(j));
                }

                movies.add(new Movie(
                        movieJson.getInt("id"),
                        movieJson.getString("title"),
                        movieJson.optString("release_date", "N/A"),
                        movieJson.optDouble("vote_average", 0.0),
                        movieJson.optInt("vote_count", 0),
                        genreIds
                ));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return movies;
    }
}
