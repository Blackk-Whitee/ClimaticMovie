package org.example.infrastructure.storage;

import org.example.domain.models.Event;
import org.example.domain.repositories.EventRepository;

import java.io.IOException;
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

            createDirectory(dirPath);

            Path filePath = getFilePath(dirPath, date);

            Files.writeString(filePath, event.getRawData() + System.lineSeparator(), StandardOpenOption.CREATE, StandardOpenOption.APPEND, StandardOpenOption.DSYNC);

            System.out.println("Evento guardado exitosamente");
        } catch (Exception e) {
            System.err.println("ERROR al guardar evento: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al guardar evento", e);
        }
    }

    private static Path getFilePath(Path dirPath, String date) {
        Path filePath = dirPath.resolve(date + ".events");
        System.out.println("Escribiendo en archivo: " + filePath);
        return filePath;
    }

    private static void createDirectory(Path dirPath) throws IOException {
        System.out.println("Intentando crear directorios: " + dirPath);
        Files.createDirectories(dirPath);
    }
}