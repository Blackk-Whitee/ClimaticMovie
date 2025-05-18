package org.example.domain.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EventAccumulator {
    private final List<String> weatherEvents = Collections.synchronizedList(new ArrayList<>());
    private final List<String> movieEvents = Collections.synchronizedList(new ArrayList<>());

    public void addWeatherEvent(String json) {
        weatherEvents.add(json);
    }

    public void addMovieEvent(String json) {
        movieEvents.add(json);
    }

    public List<String> getWeatherEvents() {
        return new ArrayList<>(weatherEvents);
    }

    public List<String> getMovieEvents() {
        return new ArrayList<>(movieEvents);
    }

    public void clear() {
        weatherEvents.clear();
        movieEvents.clear();
    }
}
