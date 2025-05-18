package org.example.domain.services;
import org.example.infrastructure.store.SQLiteDatamart;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.*;

class DataProcessingServiceTest {

    private DatamartUpdater datamartUpdater;
    private DataProcessingService service;

    @BeforeEach
    void setUp() {
        // Mocks para los componentes reales que no necesitamos testear aquí
        SQLiteDatamart mockDatamart = mock(SQLiteDatamart.class);
        RecommendationService mockRecommendationService = mock(RecommendationService.class);

        // Espiamos el updater para luego reemplazarlo en el servicio con uno mockeado
        service = new DataProcessingService(mockDatamart, mockRecommendationService);

        // Reemplazo reflejado del DatamartUpdater interno por un mock
        datamartUpdater = mock(DatamartUpdater.class);
        try {
            var field = DataProcessingService.class.getDeclaredField("datamartUpdater");
            field.setAccessible(true);
            field.set(service, datamartUpdater);
        } catch (Exception e) {
            throw new RuntimeException("No se pudo modificar el campo datamartUpdater", e);
        }
    }

    @Test
    void processHistoricalData_shouldParseValidDataAndIgnoreInvalid() {
        List<String> weatherEvents = List.of("{\"city\":\"Madrid\",\"temperature\":25.5,\"humidity\":60,\"condition\":\"Soleado\",\"ts\":\"2025-05-18T10:15:30Z\"}");
        List<String> movieEvents = List.of("{\"title\":\"Inception\",\"releaseDate\":\"2010-07-16\",\"voteAverage\":8.8,\"genres\":[\"Sci-Fi\",\"Thriller\"]}");


        service.processHistoricalData(weatherEvents, movieEvents);

        verify(datamartUpdater).updateDatamart(
                argThat(list -> list.size() == 1 && "Madrid".equals(list.get(0).getCity())),
                argThat(list -> list.size() == 1 && "Inception".equals(list.get(0).getTitle()))
        );
    }

}
