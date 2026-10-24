package org.ulpgc.climaticmovie.feedermovie;

import org.ulpgc.climaticmovie.feedermovie.application.controller.Control;
import org.ulpgc.climaticmovie.feedermovie.infrastructure.api.TmdbMovieProvider;
import org.ulpgc.climaticmovie.feedermovie.infrastructure.store.ActiveMQMovieStore;

public class Main {
    private static final String API_KEY_ENV = "TMDB_API_KEY";
    private static final String TOPIC = "movies.Trending";

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.err.println("Uso: java -jar feeder-movie.jar <broker_url>");
            System.err.println("La API key se lee de la variable de entorno " + API_KEY_ENV);
            System.exit(1);
        }
        String apiKey = System.getenv(API_KEY_ENV);
        if (apiKey == null || apiKey.isBlank()) {
            System.err.println("Falta la variable de entorno " + API_KEY_ENV);
            System.exit(1);
        }

        ActiveMQMovieStore store = new ActiveMQMovieStore(args[0], TOPIC);
        Runtime.getRuntime().addShutdownHook(new Thread(store::close));
        new Control(new TmdbMovieProvider(apiKey), store).startDailyFetch();
        System.out.println("feeder-movie iniciado: primera descarga ahora y después cada 24 horas.");
    }
}
