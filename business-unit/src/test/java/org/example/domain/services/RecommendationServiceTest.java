package org.example.domain.services;

import org.example.domain.models.Movie;
import org.example.domain.models.Recommendation;
import org.example.domain.models.Weather;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RecommendationServiceTest {

    private RecommendationService service;

    @BeforeEach
    void setUp() {
        service = new RecommendationService();
    }

    @Test
    void generateRecommendations_shouldReturnEmptyList_whenNoMatchingGenres() {
        Weather weather = new Weather("Bilbao", 15.0, 80, "Lluvia", Instant.now());
        // Lluvia podría esperar géneros tristes, pero damos solo "Acción"
        Movie movie = new Movie("Acción Innecesaria", null, 7.0, List.of("Acción"));
        List<Recommendation> recommendations = service.generateRecommendations(
                List.of(weather), List.of(movie)
        );

        assertEquals(1, recommendations.size());
        Recommendation rec = recommendations.get(0);
        assertTrue(rec.getRecommendedMovies().isEmpty());
    }

    @Test
    void generateRecommendations_shouldHandleEmptyInputs() {
        List<Recommendation> result1 = service.generateRecommendations(List.of(), List.of());
        List<Recommendation> result2 = service.generateRecommendations(List.of(), List.of(new Movie("a", null, 1, List.of())));
        List<Recommendation> result3 = service.generateRecommendations(List.of(new Weather("a", 1, 1, "Nublado", Instant.now())), List.of());
        assertTrue(result1.isEmpty());
        assertTrue(result2.isEmpty());
        assertEquals(1, result3.size());
        assertTrue(result3.get(0).getRecommendedMovies().isEmpty());
    }
}
