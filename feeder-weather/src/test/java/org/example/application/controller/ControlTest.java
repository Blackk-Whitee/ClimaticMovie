package org.example.application.controller;

import org.example.domain.models.Weather;
import org.example.infrastructure.api.WeatherProvider;
import org.example.infrastructure.messaging.ActiveMQEventStorer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

public class ControlTest {

    private WeatherProvider weatherProvider;
    private ActiveMQEventStorer eventStorer;
    private Control control;

    @BeforeEach
    void setup() {
        weatherProvider = mock(WeatherProvider.class);
        eventStorer = mock(ActiveMQEventStorer.class);
        control = new Control(weatherProvider, eventStorer, "testSource");
    }

    @Test
    void testFetchAndSaveWeather_CallsWeatherProvider() throws Exception {
        Weather dummyWeather = new Weather("weatherTopic", "CityX", 25.0, 60, "Sunny", 10.5, LocalDateTime.now());
        when(weatherProvider.fetchWeather("CityX")).thenReturn(dummyWeather);

        control.fetchAndSaveWeather("CityX");

        verify(weatherProvider).fetchWeather("CityX");
    }

    @Test
    void testFetchAndSaveWeather_PublishesEvent() throws Exception {
        Weather dummyWeather = new Weather("weatherTopic", "CityX", 25.0, 60, "Sunny", 10.5, LocalDateTime.now());
        when(weatherProvider.fetchWeather("CityX")).thenReturn(dummyWeather);

        control.fetchAndSaveWeather("CityX");

        verify(eventStorer).publishWeatherEvent(dummyWeather, "testSource");
    }

    @Test
    void testFetchAndSaveWeather_WhenExceptionThrown_DoesNotThrow() throws Exception {
        when(weatherProvider.fetchWeather("CityX")).thenThrow(new RuntimeException("API failed"));

        control.fetchAndSaveWeather("CityX");

        // No exception expected; verify that eventStorer is never called
        verify(eventStorer, never()).publishWeatherEvent(any(), any());
    }
}
