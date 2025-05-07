package org.example.infrastructure.storage;

import org.example.domain.models.Event;
import org.example.domain.repositories.EventRepository;
import java.nio.file.*;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class FileEventWriter implements EventRepository {
    private static final String BASE_DIR = "eventstore";

    @Override
    public void saveEvent(Event event) {
        try {
            String date = event.getTimestamp().atZone(ZoneId.of("UTC")).format(DateTimeFormatter.ofPattern("yyyyMMdd"));

            Path dirPath = Paths.get(BASE_DIR, event.getTopic(), event.getSource());
            Files.createDirectories(dirPath);

            Path filePath = dirPath.resolve(date + ".events");
            Files.writeString(filePath, event.getRawData() + System.lineSeparator(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar evento", e);
        }
    }
}