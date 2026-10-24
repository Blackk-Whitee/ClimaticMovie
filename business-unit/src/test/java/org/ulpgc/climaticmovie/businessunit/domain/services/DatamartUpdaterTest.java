package org.ulpgc.climaticmovie.businessunit.domain.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ulpgc.climaticmovie.businessunit.domain.models.*;
import org.ulpgc.climaticmovie.businessunit.infrastructure.store.SQLiteDatamart;

import java.time.Instant;
import java.time.LocalDate;
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
    void updateDatamart_storesDataAndRegeneratesRecommendationsFromWholeDatamart() {
        List<Weather> weatherData = List.of(new Weather("Madrid", 25.0, 40, "Sunny", Instant.now()));
        List<Movie> movieData = List.of(new Movie("Inception", LocalDate.of(2010, 7, 16), 8.8, List.of("Aventura")));
        List<Weather> allWeather = List.of(weatherData.get(0), new Weather("Bilbao", 15.0, 80, "Light rain", Instant.now()));
        List<Recommendation> recommendations = List.of(new Recommendation("Madrid", "Sunny", List.of("Inception")));

        when(datamart.getAllWeatherData()).thenReturn(allWeather);
        when(datamart.getAllMovies()).thenReturn(movieData);
        when(recommendationService.generateRecommendations(allWeather, movieData)).thenReturn(recommendations);

        updater.updateDatamart(weatherData, movieData);

        verify(datamart).upsertWeatherData(weatherData);
        verify(datamart).replaceMovies(movieData);
        verify(recommendationService).generateRecommendations(allWeather, movieData);
        verify(datamart).updateRecommendations(recommendations);
    }

    @Test
    void updateDatamart_onlyWeather_keepsStoredMovies() {
        List<Weather> weatherData = List.of(new Weather("Madrid", 25.0, 40, "Sunny", Instant.now()));

        updater.updateDatamart(weatherData, List.of());

        verify(datamart).upsertWeatherData(weatherData);
        verify(datamart, never()).replaceMovies(any());
        verify(datamart).updateRecommendations(any());
    }

    @Test
    void updateDatamart_noData_doesNothing() {
        updater.updateDatamart(List.of(), List.of());

        verifyNoInteractions(datamart, recommendationService);
    }
}
