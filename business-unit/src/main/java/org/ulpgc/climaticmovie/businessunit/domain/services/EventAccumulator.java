package org.ulpgc.climaticmovie.businessunit.domain.services;

import java.util.ArrayList;
import java.util.List;

public class EventAccumulator {
    private final List<String> weatherEvents = new ArrayList<>();
    private final List<String> movieEvents = new ArrayList<>();

    public synchronized void addWeatherEvent(String json) {
        weatherEvents.add(json);
    }

    public synchronized void addMovieEvent(String json) {
        movieEvents.add(json);
    }

    public synchronized List<String> getWeatherEvents() {
        return new ArrayList<>(weatherEvents);
    }

    public synchronized List<String> getMovieEvents() {
        return new ArrayList<>(movieEvents);
    }

    /** Devuelve y vacía los eventos acumulados de forma atómica, sin perder los que lleguen mientras se procesan. */
    public synchronized Batch drain() {
        Batch batch = new Batch(new ArrayList<>(weatherEvents), new ArrayList<>(movieEvents));
        clear();
        return batch;
    }

    public synchronized void clear() {
        weatherEvents.clear();
        movieEvents.clear();
    }

    public record Batch(List<String> weatherEvents, List<String> movieEvents) { }
}
