package infrastructure.api;
import domain.models.Movie;
import org.json.JSONObject;
import java.net.URL;
import java.time.Instant;
import java.util.*;
import java.util.stream.*;

public class TmdbMovieProvider implements MovieProvider{
    private final String apiKey;
    private static final String BASE_URL = "https://api.themoviedb.org/3/trending/movie/day?language=es-ES";
    private final Map<Integer, String> genres;
    private static final String GENRE_LIST_URL = "https://api.themoviedb.org/3/genre/movie/list?language=es-ES";
    private static final int MAX_PAGES = 5;

    public TmdbMovieProvider(String apiKey) {
        this.apiKey = apiKey;
        genres = this.fetchGenres();
    }

    @Override
    public List<Movie> provide() {
        List<Movie> allMovies = new ArrayList<>();
        for (int page = 1; page <= MAX_PAGES; page++) {
            try {
                String json = new Scanner(new URL(BASE_URL + "&api_key=" + apiKey + "&page=" + page).openStream())
                        .useDelimiter("\\A").next();
                List<Movie> pageMovies = IntStream.range(0, new JSONObject(json).getJSONArray("results").length())
                        .mapToObj(i -> new JSONObject(json).getJSONArray("results").getJSONObject(i))
                        .map(this::mapToMovie)
                        .collect(Collectors.toList());
                allMovies.addAll(pageMovies);
            } catch (Exception e) {
                continue;
            }
        }
        return allMovies;
    }

    private Movie mapToMovie(JSONObject obj) {
        return new Movie(
                "movies.Trending",
                Instant.now().toString(),                     // ts
                "feeder movie",                   // ss (valor por defecto o personalizado)
                obj.getString("title"),            // title
                obj.optString("release_date", "N/A"), // releaseDate
                obj.optDouble("vote_average", 0.0), // voteAverage
                genres(obj)                        // genres
        );
    }

    private List<String> genres(JSONObject obj) {
        return obj.getJSONArray("genre_ids").toList().stream()
                .map(j -> genres.get(j))
                .collect(Collectors.toList());
    }

    private Map<Integer, String> fetchGenres() {
        try {
            String json = new Scanner(new URL(GENRE_LIST_URL + "&api_key=" + apiKey).openStream()).useDelimiter("\\A").next();
            Map<Integer, String> genres = new HashMap<>();
            new JSONObject(json).getJSONArray("genres").forEach(g -> genres.put(((JSONObject)g).getInt("id"), ((JSONObject)g).getString("name")));
            return genres;
        } catch (Exception e) { return new HashMap<>(); }
    }

}