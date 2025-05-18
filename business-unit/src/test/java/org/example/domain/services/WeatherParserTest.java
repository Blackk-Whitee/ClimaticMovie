package org.example.domain.services;

import org.example.domain.models.Weather;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class WeatherParserTest {

    private final WeatherParser parser = new WeatherParser();

    @Test
    void parse_shouldReturnWeatherObject_givenValidJson() {
        String json = """
            {
              "city": "Barcelona",
              "temperature": 22.5,
              "humidity": 60,
              "condition": "Soleado",
              "ts": "2023-05-16T14:30:00Z"
            }
        """;
        Weather weather = parser.parse(json);
        assertNotNull(weather);
        assertEquals("Barcelona", weather.getCity());
        assertEquals(22.5, weather.getTemperature());
        assertEquals(60, weather.getHumidity());
        assertEquals("Soleado", weather.getCondition());
        assertEquals(Instant.parse("2023-05-16T14:30:00Z"), weather.getTimestamp());
    }

    @Test
    void parse_shouldReturnNull_givenInvalidJson() {
        String invalidJson = "esto no es un JSON válido";
        Weather result = parser.parse(invalidJson);
        assertNull(result);
    }

    @Test
    void parse_shouldReturnNull_givenInvalidTimestamp() {
        String badTimestampJson = """
            {
              "city": "Madrid",
              "temperature": 25.0,
              "humidity": 55,
              "condition": "Nublado",
              "ts": "16-05-2023 14:30"
            }
        """;
        Weather result = parser.parse(badTimestampJson);
        assertNull(result);
    }

    @Test
    void parse_shouldIgnoreExtraFields() {
        String jsonWithExtras = """
            {
              "city": "Valencia",
              "temperature": 20.0,
              "humidity": 50,
              "condition": "Lluvia",
              "ts": "2023-05-16T12:00:00Z",
              "extra": "dato que se ignora"
            }
        """;
        Weather weather = parser.parse(jsonWithExtras);
        assertNotNull(weather);
        assertEquals("Valencia", weather.getCity());
    }
}
