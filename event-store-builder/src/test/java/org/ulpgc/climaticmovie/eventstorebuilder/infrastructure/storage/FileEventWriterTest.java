package org.ulpgc.climaticmovie.eventstorebuilder.infrastructure.storage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.ulpgc.climaticmovie.eventstorebuilder.domain.models.Event;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileEventWriterTest {

    @TempDir
    Path baseDir;

    @Test
    void shouldWriteEventToFile() throws IOException {
        FileEventWriter writer = new FileEventWriter(baseDir);
        Event event = new Event("test-topic", "api", Instant.parse("2024-05-17T12:00:00Z"), "{\"message\":\"test\"}");

        writer.saveEvent(event);

        Path expectedFile = baseDir.resolve("test-topic/api/20240517.events");
        assertTrue(Files.exists(expectedFile));
        assertEquals(List.of("{\"message\":\"test\"}"), Files.readAllLines(expectedFile));
    }

    @Test
    void shouldNotWriteOutsideBaseDir() throws IOException {
        FileEventWriter writer = new FileEventWriter(baseDir);
        Event event = new Event("../../evil", "..", Instant.parse("2024-05-17T12:00:00Z"), "{}");

        assertThrows(IllegalArgumentException.class, () -> writer.saveEvent(event));

        writer.saveEvent(new Event("../x", "a/b", Instant.parse("2024-05-17T12:00:00Z"), "{}"));
        try (var files = Files.walk(baseDir)) {
            assertTrue(files.allMatch(p -> p.normalize().startsWith(baseDir)));
        }
    }
}
