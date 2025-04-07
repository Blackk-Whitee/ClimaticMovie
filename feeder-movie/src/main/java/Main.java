import core.usecases.FetchGenres;
import core.usecases.FetchMovies;
import infrastructure.Config;
import infrastructure.api.GenreApiClient;
import infrastructure.api.MovieApiClient;
import infrastructure.db.SQLiteGenreRepository;
import infrastructure.db.SQLiteMovieRepository;
import infrastructure.db.SQLiteConnection;


public class Main {
    public static void main(String[] args) {
        // 1. Inicializar la base de datos y tablas
        SQLiteConnection.initializeDatabase();

        // 2. Resto del código (ejecución de casos de uso)...
        String apiKey = Config.getApiKey();
        GenreApiClient genreApiClient = new GenreApiClient(apiKey);
        MovieApiClient movieApiClient = new MovieApiClient(apiKey);
        SQLiteGenreRepository genreRepo = new SQLiteGenreRepository();
        SQLiteMovieRepository movieRepo = new SQLiteMovieRepository();

        new FetchGenres(genreApiClient, genreRepo).execute();
        new FetchMovies(movieApiClient, movieRepo).execute(20);
    }
}
