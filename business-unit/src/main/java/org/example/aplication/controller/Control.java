package org.example.aplication.controller;

import org.example.domain.services.DataProcessingService;
import org.example.domain.services.RecommendationService;
import org.example.infrastructure.messaging.BusinessUnitActiveMQConsumer;
import org.example.infrastructure.store.SQLiteDatamart;
import org.example.infrastructure.messaging.ActiveMQRealTimeConsumer;
import org.example.infrastructure.store.EventStoreReader;
import org.example.aplication.cli.CLIInterface;

import java.util.Collections;
import java.util.List;

public class Control {
    private final DataProcessingService dataProcessingService;
    private final RecommendationService recommendationService;
    private final CLIInterface cliInterface;

    public Control() {
        SQLiteDatamart datamart = new SQLiteDatamart();
        this.recommendationService = new RecommendationService();
        this.dataProcessingService = new DataProcessingService(datamart, recommendationService);
        this.cliInterface = new CLIInterface(datamart);

        // Cargar solo los últimos archivos de cada topic al iniciar
        EventStoreReader eventStoreReader = new EventStoreReader();
        dataProcessingService.processHistoricalData(
                eventStoreReader.readWeatherEvents(),
                eventStoreReader.readMovieEvents());
    }


    public void start(String brokerUrl) {
        // Iniciar consumidor de tiempo real
        BusinessUnitActiveMQConsumer realTimeConsumer = new BusinessUnitActiveMQConsumer(brokerUrl, this);
        realTimeConsumer.start();


        // Iniciar interfaz de usuario
        cliInterface.start();
    }

    public void updateWeatherData(String jsonWeather) {
        dataProcessingService.processNewWeatherData(jsonWeather);
    }

    public void updateMoviesData(String jsonMovies) {
        dataProcessingService.processNewMovieData(jsonMovies);
    }
}