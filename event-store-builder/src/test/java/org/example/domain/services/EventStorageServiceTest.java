package org.example.domain.services;

import org.example.domain.models.Event;
import org.example.domain.repositories.EventRepository;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.mockito.Mockito.*;

class EventStorageServiceTest {

    @Test
    void shouldCallRepositoryToSaveEvent() {
        EventRepository mockRepo = mock(EventRepository.class);
        EventStorageService service = new EventStorageService(mockRepo);

        Event event = new Event("test", "source", Instant.now(), "{}");
        service.processAndStoreEvent(event);

        verify(mockRepo, times(1)).saveEvent(event);
    }
}
