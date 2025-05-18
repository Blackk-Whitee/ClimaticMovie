package org.example.application;

import org.example.domain.models.Event;
import org.example.domain.services.EventStorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.jms.TextMessage;
import java.util.List;

import static org.mockito.Mockito.*;

class EventConsumerTest {

    private EventStorageService storageService;
    private EventConsumer consumer;

    @BeforeEach
    void setUp() {
        storageService = mock(EventStorageService.class);
        consumer = new EventConsumer(storageService, "tcp://localhost:61616", List.of("test-topic"));
    }

    @Test
    void shouldParseJsonAndProcessEvent() throws Exception {
        String json = """
            {
              "topic": "test-topic",
              "ss": "api",
              "ts": "2024-05-17T12:00:00Z"
            }
        """;

        TextMessage message = mock(TextMessage.class);
        when(message.getText()).thenReturn(json);

        consumer.onMessage(message);

        verify(storageService, times(1)).processAndStoreEvent(any(Event.class));
        verify(message, times(1)).acknowledge();
    }
}
