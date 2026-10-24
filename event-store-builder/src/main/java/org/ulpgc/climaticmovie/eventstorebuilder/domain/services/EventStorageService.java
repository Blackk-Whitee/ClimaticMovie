package org.ulpgc.climaticmovie.eventstorebuilder.domain.services;

import org.ulpgc.climaticmovie.eventstorebuilder.domain.models.Event;
import org.ulpgc.climaticmovie.eventstorebuilder.domain.repositories.EventRepository;

public class EventStorageService {
    private final EventRepository repository;

    public EventStorageService(EventRepository repository) {
        this.repository = repository;
    }

    public void processAndStoreEvent(Event event) {
        repository.saveEvent(event);
    }
}