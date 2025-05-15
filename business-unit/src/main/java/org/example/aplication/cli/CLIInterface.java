package org.example.aplication.cli;

import org.example.domain.models.Recommendation;
import org.example.infrastructure.store.SQLiteDatamart;
import java.util.List;
import java.util.Scanner;

public class CLIInterface {
    private final SQLiteDatamart datamart;
    private final Scanner scanner;

    public CLIInterface(SQLiteDatamart datamart) {
        this.datamart = datamart;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("=== Sistema de Recomendación de Películas por Clima ===");
        System.out.println("Ciudades disponibles en España. Ingrese una para obtener recomendaciones.");

        while (true) {
            System.out.print("\nIngrese una ciudad (o 'salir' para terminar): ");
            String city = scanner.nextLine();

            if ("salir".equalsIgnoreCase(city)) {
                break;
            }

            Recommendation recommendation = datamart.getRecommendationForCity(city);

            if (recommendation == null) {
                System.out.println("No hay datos disponibles para " + city);
            } else {
                printRecommendation(recommendation);
            }
        }

        System.out.println("Gracias por usar nuestro sistema de recomendación.");
        scanner.close();
    }

    private void printRecommendation(Recommendation recommendation) {
        System.out.println("\n--- Recomendaciones para " + recommendation.getCity() + " ---");
        System.out.println("Condición climática: " + recommendation.getWeatherCondition());
        System.out.println("Películas recomendadas:");

        int counter = 1;
        for (String movie : recommendation.getRecommendedMovies()) {
            System.out.println(counter + ". " + movie);
            counter++;
        }
    }
}