package org.example.application;

import org.example.domain.models.Event;
import org.example.domain.services.EventStorageService;
import org.example.infrastructure.messaging.ActiveMQEventConsumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.time.Instant;

public class EventConsumer implements MessageListener {
    private final EventStorageService storageService;
    private final String brokerUrl;

    public EventConsumer(EventStorageService storageService, String brokerUrl) {
        this.storageService = storageService;
        this.brokerUrl = brokerUrl;
    }

    public void start() {
        ActiveMQEventConsumer consumer = new ActiveMQEventConsumer(brokerUrl, "Weather", this);
        consumer.listen();
    }

    @Override
    public void onMessage(Message message) {
        try {
            String json = ((TextMessage) message).getText();
            Event event = parseToEvent(json);
            storageService.processAndStoreEvent(event);
        } catch (Exception e) {
            System.err.println("Error procesando mensaje: " + e.getMessage());
        }
    }

    private Event parseToEvent(String json) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node;
        try {
            node = mapper.readTree(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return new Event(node.get("topic").asText(), node.get("ss").asText(), Instant.parse(node.get("ts").asText()), json);
    }
}