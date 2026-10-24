package org.ulpgc.climaticmovie.feederweather;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MainTest {

    @Test
    void readCities_supportsQuotedCsvAndMultipleLines(@TempDir Path dir) throws Exception {
        Path file = dir.resolve("cities.csv");
        Files.writeString(file, "\"Madrid\", \"Barcelona\"\nSevilla\n\n");

        assertEquals(List.of("Madrid", "Barcelona", "Sevilla"), Main.readCities(file));
    }
}
