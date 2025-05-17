package org.example.infrastructure.storage;

import org.example.domain.models.Event;
import org.junit.jupiter.api.*;
import java.io.IOException;
import java.nio.file.*;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileEventWriterTest {

    private static final Path BASE_PATH = Paths.get("eventstore/test-topic/api");

    @BeforeEach
    void cleanup() throws IOException {
        if (Files.exists(BASE_PATH)) {
            Files.walk(BASE_PATH)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(java.io.File::delete);
        }
    }

    @Test
    void shouldWriteEventToFile() throws IOException {
        FileEventWriter writer = new FileEventWriter();
        Event event = new Event("test-topic", "api", Instant.parse("2024-05-17T12:00:00Z"), "{\"message\":\"test\"}");

        writer.saveEvent(event);

        Path expectedFile = BASE_PATH.resolve("20240517.events");
        assertTrue(Files.exists(expectedFile));

        List<String> lines = Files.readAllLines(expectedFile);
        assertTrue(lines.contains("{\"message\":\"test\"}"));
    }
}
