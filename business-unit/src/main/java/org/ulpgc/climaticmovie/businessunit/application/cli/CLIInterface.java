package org.ulpgc.climaticmovie.businessunit.application.cli;

import org.ulpgc.climaticmovie.businessunit.domain.models.Recommendation;
import org.ulpgc.climaticmovie.businessunit.infrastructure.store.SQLiteDatamart;

import java.util.List;
import java.util.Scanner;

public class CLIInterface {
    private static final int MAX_MOVIES_SHOWN = 10;

    private final SQLiteDatamart datamart;
    private final Scanner scanner;

    public CLIInterface(SQLiteDatamart datamart) {
        this.datamart = datamart;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("=== Sistema de Recomendación de Películas por Clima ===");
        System.out.println("Ciudades disponibles: " + String.join(", ", datamart.getAvailableCities()));
        while (true) {
            System.out.print("\nIngrese una ciudad (o 'salir' para terminar): ");
            if (!scanner.hasNextLine()) break;
            String city = scanner.nextLine().trim();
            if (city.isEmpty()) continue;
            if ("salir".equalsIgnoreCase(city)) break;

            Recommendation recommendation = datamart.getRecommendationForCity(city);
            if (recommendation == null) {
                System.out.println("No hay datos disponibles para " + city);
            } else {
                printRecommendation(recommendation);
            }
        }
        System.out.println("Gracias por usar nuestro sistema de recomendación.");
    }

    private void printRecommendation(Recommendation recommendation) {
        System.out.println("\n--- Recomendaciones para " + recommendation.getCity() + " ---");
        System.out.println("Condición climática: " + recommendation.getWeatherCondition());
        List<String> movies = recommendation.getRecommendedMovies();
        if (movies.isEmpty()) {
            System.out.println("No hay películas que encajen con este clima ahora mismo.");
            return;
        }
        System.out.println("Películas recomendadas:");
        for (int i = 0; i < Math.min(movies.size(), MAX_MOVIES_SHOWN); i++) {
            System.out.println((i + 1) + ". " + movies.get(i));
        }
    }
}
