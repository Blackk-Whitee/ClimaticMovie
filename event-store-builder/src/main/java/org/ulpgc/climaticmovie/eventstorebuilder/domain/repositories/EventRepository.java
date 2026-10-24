package org.ulpgc.climaticmovie.eventstorebuilder.domain.repositories;

import org.ulpgc.climaticmovie.eventstorebuilder.domain.models.Event;

public interface EventRepository {
    void saveEvent(Event event);
}