package infrastructure.api;
import org.json.JSONArray;
import org.json.JSONObject;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class GenreApiClient {
    private final String apiKey;
    private static final String GENRE_LIST_URL = "https://api.themoviedb.org/3/genre/movie/list?language=es-ES";

    public GenreApiClient(String apiKey) {
        this.apiKey = apiKey;
    }

    public Map<Integer, String> fetchGenres() {
        try {
            String json = new Scanner(new URL(GENRE_LIST_URL + "&api_key=" + apiKey).openStream()).useDelimiter("\\A").next();
            Map<Integer, String> genres = new HashMap<>();
            new JSONObject(json).getJSONArray("genres").forEach(g -> genres.put(((JSONObject)g).getInt("id"), ((JSONObject)g).getString("name")));
            return genres;
        } catch (Exception e) { return new HashMap<>(); }
    }
}