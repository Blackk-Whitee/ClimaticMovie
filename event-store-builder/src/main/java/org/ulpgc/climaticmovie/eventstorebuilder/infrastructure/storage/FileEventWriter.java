package org.ulpgc.climaticmovie.eventstorebuilder.infrastructure.storage;

import org.ulpgc.climaticmovie.eventstorebuilder.domain.models.Event;
import org.ulpgc.climaticmovie.eventstorebuilder.domain.repositories.EventRepository;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.*;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class FileEventWriter implements EventRepository {
    private static final DateTimeFormatter DAY = DateTimeFormatter.ofPattern("yyyyMMdd").withZone(ZoneOffset.UTC);

    private final Path baseDir;

    public FileEventWriter() {
        this(Paths.get("eventstore"));
    }

    public FileEventWriter(Path baseDir) {
        this.baseDir = baseDir;
    }

    @Override
    public synchronized void saveEvent(Event event) {
        try {
            Path dirPath = baseDir.resolve(safeName(event.getTopic())).resolve(safeName(event.getSource()));
            Files.createDirectories(dirPath);
            Path filePath = dirPath.resolve(DAY.format(event.getTimestamp()) + ".events");
            Files.writeString(filePath, event.getRawData() + System.lineSeparator(),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new UncheckedIOException("Error al guardar evento", e);
        }
    }

    // topic y ss vienen del mensaje: se limitan a caracteres seguros para evitar escribir fuera de baseDir
    static String safeName(String value) {
        String cleaned = value == null ? "" : value.replaceAll("[^A-Za-z0-9._ -]", "_").trim();
        if (cleaned.isEmpty() || cleaned.chars().allMatch(c -> c == '.')) {
            throw new IllegalArgumentException("Nombre de topic/fuente no válido: " + value);
        }
        return cleaned;
    }
}
