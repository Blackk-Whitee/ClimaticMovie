package org.ulpgc.climaticmovie.businessunit.infrastructure.store;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.ulpgc.climaticmovie.businessunit.domain.models.*;

import java.nio.file.Path;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SQLiteDatamartTest {

    @TempDir
    Path dir;

    private SQLiteDatamart datamart;

    @BeforeEach
    void setUp() {
        datamart = new SQLiteDatamart("jdbc:sqlite:" + dir.resolve("test.db"));
    }

    @Test
    void recommendationLookupIgnoresCaseAndSpaces() {
        datamart.updateRecommendations(List.of(new Recommendation("Madrid", "Sunny", List.of("A", "B"))));

        Recommendation rec = datamart.getRecommendationForCity("  madrid ");

        assertNotNull(rec);
        assertEquals(List.of("A", "B"), rec.getRecommendedMovies());
    }

    @Test
    void weatherUpsertKeepsOtherCities() {
        datamart.upsertWeatherData(List.of(
                new Weather("Madrid", 20, 40, "Sunny", Instant.parse("2025-05-18T10:00:00Z")),
                new Weather("Bilbao", 15, 80, "Light rain", Instant.parse("2025-05-18T10:00:00Z"))));
        datamart.upsertWeatherData(List.of(
                new Weather("Madrid", 22, 35, "Clear", Instant.parse("2025-05-18T11:00:00Z"))));

        List<Weather> all = datamart.getAllWeatherData();

        assertEquals(2, all.size());
        assertEquals("Clear", all.stream().filter(w -> w.getCity().equals("Madrid")).findFirst().orElseThrow().getCondition());
    }

    @Test
    void moviesWithoutReleaseDateAreStoredAndRead() {
        datamart.replaceMovies(List.of(new Movie("Sin fecha", null, 6.5, List.of("Drama"))));

        List<Movie> movies = datamart.getAllMovies();

        assertEquals(1, movies.size());
        assertNull(movies.get(0).getReleaseDate());
        assertEquals(List.of("Drama"), movies.get(0).getGenres());
    }

    @Test
    void emptyRecommendationListIsReadAsEmpty() {
        datamart.updateRecommendations(List.of(new Recommendation("Sevilla", "Sunny", List.of())));

        assertTrue(datamart.getRecommendationForCity("Sevilla").getRecommendedMovies().isEmpty());
    }
}
