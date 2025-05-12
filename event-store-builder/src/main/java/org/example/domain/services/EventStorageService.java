package org.example.domain.services;

import org.example.domain.models.Event;
import org.example.domain.repositories.EventRepository;

public class EventStorageService {
    private final EventRepository repository;

    public EventStorageService(EventRepository repository) {
        this.repository = repository;
    }

    public void processAndStoreEvent(Event event) {
        repository.saveEvent(event);
    }
}