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
import java.util.List;

public class EventConsumer implements MessageListener {
    private final EventStorageService storageService;
    private final String brokerUrl;
    private final List<String> topics;
    private ActiveMQEventConsumer consumer;


    public EventConsumer(EventStorageService storageService, String brokerUrl, List<String> topics) {
        this.storageService = storageService;
        this.brokerUrl = brokerUrl;
        this.topics = topics;
    }

    public void start() {
        this.consumer = new ActiveMQEventConsumer(brokerUrl, topics, this);
        consumer.listen();
    }

    public void shutdown() {
        if (consumer != null) {
            consumer.shutdown();
        }
    }

    @Override
    public void onMessage(Message message) {
        try {
            System.out.println("Nuevo mensaje recibido: " + Instant.now());
            String json = ((TextMessage) message).getText();
            Event event = parseToEvent(json);
            storageService.processAndStoreEvent(event);

            message.acknowledge();
            System.out.println("Evento procesado y confirmado: " + event.getTopic());
        } catch (Exception e) {
            System.err.println("Error procesando mensaje: " + e.getMessage());
            e.printStackTrace();
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