package infrastructure.api;
import core.Movie;
import org.json.JSONObject;
import java.net.URL;
import java.util.*;
import java.util.stream.*;

public class MovieApiClient {
    private final String apiKey;
    private static final String BASE_URL = "https://api.themoviedb.org/3/trending/movie/day?language=es-ES";

    public MovieApiClient(String apiKey) {
        this.apiKey = apiKey;
    }

    public List<Movie> fetchTrendingMovies(int page) {
        try {
            String json = new Scanner(new URL(BASE_URL + "&api_key=" + apiKey + "&page=" + page).openStream()).useDelimiter("\\A").next();
            return IntStream.range(0, new JSONObject(json).getJSONArray("results").length())
                    .mapToObj(i -> new JSONObject(json).getJSONArray("results").getJSONObject(i))
                    .map(obj -> new Movie(
                            obj.getInt("id"),
                            obj.getString("title"),
                            obj.optString("release_date", "N/A"),
                            obj.optDouble("vote_average", 0.0),
                            obj.optInt("vote_count", 0),
                            IntStream.range(0, obj.getJSONArray("genre_ids").length())
                                    .map(j -> obj.getJSONArray("genre_ids").getInt(j))
                                    .boxed().collect(Collectors.toList())
                    )).collect(Collectors.toList());
        } catch (Exception e) { return new ArrayList<>(); }
    }
}