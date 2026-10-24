package org.ulpgc.climaticmovie.businessunit.application.controller;

import org.ulpgc.climaticmovie.businessunit.application.cli.CLIInterface;
import org.ulpgc.climaticmovie.businessunit.domain.services.DataProcessingService;
import org.ulpgc.climaticmovie.businessunit.domain.services.RecommendationService;
import org.ulpgc.climaticmovie.businessunit.infrastructure.messaging.BusinessUnitActiveMQConsumer;
import org.ulpgc.climaticmovie.businessunit.infrastructure.store.EventStoreReader;
import org.ulpgc.climaticmovie.businessunit.infrastructure.store.SQLiteDatamart;

import java.util.List;

public class Control {
    private final DataProcessingService dataProcessingService;
    private final CLIInterface cliInterface;

    public Control(String dbUrl) {
        SQLiteDatamart datamart = new SQLiteDatamart(dbUrl);
        this.dataProcessingService = new DataProcessingService(datamart, new RecommendationService());
        this.cliInterface = new CLIInterface(datamart);

        EventStoreReader eventStoreReader = new EventStoreReader();
        dataProcessingService.processHistoricalData(
                eventStoreReader.readWeatherEvents(),
                eventStoreReader.readMovieEvents());
    }

    public void start(String brokerUrl) {
        BusinessUnitActiveMQConsumer realTimeConsumer = new BusinessUnitActiveMQConsumer(brokerUrl, this);
        realTimeConsumer.start();
        try {
            cliInterface.start();
        } finally {
            realTimeConsumer.shutdown();
        }
    }

    public void processHistoricalData(List<String> weatherEvents, List<String> movieEvents) {
        dataProcessingService.processHistoricalData(weatherEvents, movieEvents);
    }
}
