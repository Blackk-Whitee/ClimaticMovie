package org.example.domain.models;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class EventTest {

    @Test
    void shouldCreateEventWithCorrectValues() {
        Instant now = Instant.now();
        Event event = new Event("movies", "api", now, "{\"test\":\"value\"}");

        assertEquals("movies", event.getTopic());
        assertEquals("api", event.getSource());
        assertEquals(now, event.getTimestamp());
        assertEquals("{\"test\":\"value\"}", event.getRawData());
    }
}
