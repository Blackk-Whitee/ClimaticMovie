package org.example.aplication.controller;

import org.example.domain.services.DataProcessingService;
import org.example.domain.services.RecommendationService;
import org.example.infrastructure.messaging.BusinessUnitActiveMQConsumer;
import org.example.infrastructure.store.SQLiteDatamart;
import org.example.infrastructure.store.EventStoreReader;
import org.example.aplication.cli.CLIInterface;

import java.util.List;

public class Control {
    private final DataProcessingService dataProcessingService;
    private final RecommendationService recommendationService;
    private final CLIInterface cliInterface;

    public Control(String dbUrl) {
        SQLiteDatamart datamart = new SQLiteDatamart(dbUrl);
        this.recommendationService = new RecommendationService();
        this.dataProcessingService = new DataProcessingService(datamart, recommendationService);
        this.cliInterface = new CLIInterface(datamart);

        EventStoreReader eventStoreReader = new EventStoreReader();
        dataProcessingService.processHistoricalData(
                eventStoreReader.readWeatherEvents(),
                eventStoreReader.readMovieEvents());
    }

    public void start(String brokerUrl) {
        BusinessUnitActiveMQConsumer realTimeConsumer = new BusinessUnitActiveMQConsumer(brokerUrl, this);
        realTimeConsumer.start();
        cliInterface.start();
    }

    public void processHistoricalData(List<String> weatherEvents, List<String> movieEvents) {
        dataProcessingService.processHistoricalData(weatherEvents, movieEvents);
    }
}