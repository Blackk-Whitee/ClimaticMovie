package org.example.infrastructure.database;

import org.example.domain.models.Weather;
import org.junit.jupiter.api.*;

import java.io.File;
import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

class SQLiteEventStorerTest {

    private static final String DB_FILE = "test-weather.db";
    private static final String DB_URL = "jdbc:sqlite:" + DB_FILE;

    private SQLiteEventStorer storer;

    @BeforeEach
    void setup() throws SQLException {
        // Borra archivo si existe para test limpio
        File file = new File(DB_FILE);
        if (file.exists()) file.delete();

        storer = new SQLiteEventStorer(DB_URL);
    }

    @AfterEach
    void cleanup() {
        // Borra archivo tras test
        File file = new File(DB_FILE);
        if (file.exists()) file.delete();
    }

    @Test
    void tableIsCreatedOnInit() throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             ResultSet rs = conn.getMetaData().getTables(null, null, "weather_data", null)) {
            assertTrue(rs.next(), "weather_data table should exist");
        }
    }

    @Test
    void saveWeather_InsertsWeatherCorrectly() throws Exception {
        Weather weather = new Weather("topic", "City", 25.5, 70, "Clear", 10.0, java.time.LocalDateTime.now());

        storer.saveWeather(weather);

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM weather_data WHERE city = ?")) {
            stmt.setString(1, "City");
            try (ResultSet rs = stmt.executeQuery()) {
                assertTrue(rs.next(), "Should find inserted weather");
                assertEquals("City", rs.getString("city"));
                assertEquals(25.5, rs.getDouble("temperature"));
                assertEquals(70, rs.getInt("humidity"));
                assertEquals("Clear", rs.getString("condition"));
                assertEquals(10.0, rs.getDouble("wind_speed"));
            }
        }
    }
}
