package org.example.domain.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EventAccumulatorTest {

    private EventAccumulator accumulator;

    @BeforeEach
    void setUp() {
        accumulator = new EventAccumulator();
    }

    @Test
    void testAddWeatherEvent_shouldAccumulateEvent() {
        accumulator.addWeatherEvent("{\"city\":\"Madrid\",\"weather\":\"Soleado\"}");
        List<String> weather = accumulator.getWeatherEvents();

        assertEquals(1, weather.size());
        assertEquals("{\"city\":\"Madrid\",\"weather\":\"Soleado\"}", weather.get(0));
    }

    @Test
    void testAddMovieEvent_shouldAccumulateEvent() {
        accumulator.addMovieEvent("{\"title\":\"Inception\",\"genre\":\"Sci-Fi\"}");
        List<String> movies = accumulator.getMovieEvents();

        assertEquals(1, movies.size());
        assertEquals("{\"title\":\"Inception\",\"genre\":\"Sci-Fi\"}", movies.get(0));
    }

    @Test
    void testGetWeatherEvents_returnsCopyNotReference() {
        accumulator.addWeatherEvent("data");
        List<String> copy = accumulator.getWeatherEvents();
        copy.add("malicioso");  // Modificamos la copia

        List<String> original = accumulator.getWeatherEvents();
        assertEquals(1, original.size()); // Verifica que el original no se alteró
    }

    @Test
    void testClear_shouldEmptyBothLists() {
        accumulator.addWeatherEvent("clima");
        accumulator.addMovieEvent("pelicula");

        accumulator.clear();

        assertTrue(accumulator.getWeatherEvents().isEmpty());
        assertTrue(accumulator.getMovieEvents().isEmpty());
    }
}
