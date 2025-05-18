package org.example.domain.services;

import org.example.domain.models.*;
import org.example.infrastructure.store.SQLiteDatamart;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

class DatamartUpdaterTest {

    private SQLiteDatamart datamart;
    private RecommendationService recommendationService;
    private DatamartUpdater updater;

    @BeforeEach
    void setUp() {
        datamart = mock(SQLiteDatamart.class);
        recommendationService = mock(RecommendationService.class);
        updater = new DatamartUpdater(datamart, recommendationService);
    }

    @Test
    void updateDatamart_shouldUpdateAllComponentsCorrectly() {
        List<Weather> weatherData = List.of(new Weather("Madrid", 25.0, 40, "Soleado", Instant.now()));
        List<Movie> movieData = List.of(new Movie("Inception", LocalDate.of(2010, 7, 16), 8.8, List.of("Sci-Fi", "Thriller")));

        List<Recommendation> mockRecommendations = Arrays.asList(
                new Recommendation("Madrid", "Soleado", List.of("Inception"))
        );

        when(recommendationService.generateRecommendations(weatherData, movieData))
                .thenReturn(mockRecommendations);

        updater.updateDatamart(weatherData, movieData);

        // Verifica que los métodos se llamen correctamente
        verify(datamart).updateWeatherData(weatherData);
        verify(datamart).updateMovies(movieData);
        verify(recommendationService).generateRecommendations(weatherData, movieData);
        verify(datamart).updateRecommendations(mockRecommendations);
    }
}
