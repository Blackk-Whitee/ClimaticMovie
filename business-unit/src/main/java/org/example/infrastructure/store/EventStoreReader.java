package org.example.infrastructure.store;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class EventStoreReader {
    private static final Logger logger = LoggerFactory.getLogger(EventStoreReader.class);
    private static final String BASE_DIR = "eventstore";

    public List<String> readWeatherEvents() {
        return readEventsFromTopic("Weather");
    }

    public List<String> readMovieEvents() {
        return readEventsFromTopic("movies.Trending");
    }

    private List<String> readEventsFromTopic(String topic) {
        try {
            Path topicPath = Paths.get(BASE_DIR, topic);
            if (!Files.exists(topicPath)) {
                logger.warn("No se encontró el directorio del topic: {}", topic);
                return Collections.emptyList();
            }

            // Buscar archivos dentro de cualquier subdirectorio
            Optional<Path> latestFile = Files.walk(topicPath, Integer.MAX_VALUE)
                    .filter(path -> path.toString().endsWith(".events"))
                    .max(Comparator.comparing(path -> path.toFile().lastModified()));

            if (latestFile.isEmpty()) {
                logger.info("No se encontraron archivos .events para el topic: {}", topic);
                return Collections.emptyList();
            }

            logger.debug("Leyendo archivo más reciente: {}", latestFile.get());
            return Files.readAllLines(latestFile.get());

        } catch (IOException e) {
            logger.error("Error leyendo eventos del topic {}: {}", topic, e.getMessage(), e);
            return Collections.emptyList();
        }
    }

}