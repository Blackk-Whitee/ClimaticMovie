package infrastructure.api;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import infrastructure.Config;

public class GenreApiClient {
    private final String apiKey;
    private static final String GENRE_LIST_URL = "https://api.themoviedb.org/3/genre/movie/list?language=es-ES";

    public GenreApiClient(String apiKey) {
        this.apiKey = apiKey;
    }

    public Map<Integer, String> fetchGenres() {
        Map<Integer, String> genreMap = new HashMap<>();
        String urlString = GENRE_LIST_URL + "&api_key=" + apiKey;

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
            JSONArray genresArray = jsonResponse.getJSONArray("genres");

            for (int i = 0; i < genresArray.length(); i++) {
                JSONObject genre = genresArray.getJSONObject(i);
                genreMap.put(genre.getInt("id"), genre.getString("name"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return genreMap;
    }
}
