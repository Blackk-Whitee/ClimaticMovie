package org.example.domain.repositories;

import org.example.domain.models.Event;

public interface EventRepository {
    void saveEvent(Event event);
}