package org.ulpgc.climaticmovie.eventstorebuilder.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.ulpgc.climaticmovie.eventstorebuilder.domain.models.Event;
import org.ulpgc.climaticmovie.eventstorebuilder.domain.services.EventStorageService;
import org.ulpgc.climaticmovie.eventstorebuilder.infrastructure.messaging.ActiveMQEventConsumer;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.time.Instant;
import java.util.List;

public class EventConsumer implements MessageListener {
    private static final ObjectMapper MAPPER = new ObjectMapper();

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
            if (!(message instanceof TextMessage textMessage)) {
                System.err.println("Mensaje ignorado: no es de texto");
                message.acknowledge();
                return;
            }
            Event event = parseToEvent(textMessage.getText());
            storageService.processAndStoreEvent(event);
            message.acknowledge();
            System.out.println("Evento guardado: " + event.getTopic() + " (" + event.getSource() + ")");
        } catch (Exception e) {
            System.err.println("Error procesando mensaje: " + e.getMessage());
        }
    }

    private Event parseToEvent(String json) throws JsonProcessingException {
        JsonNode node = MAPPER.readTree(json);
        if (!node.hasNonNull("topic") || !node.hasNonNull("ss") || !node.hasNonNull("ts")) {
            throw new IllegalArgumentException("El evento debe incluir topic, ss y ts");
        }
        return new Event(node.get("topic").asText(), node.get("ss").asText(), Instant.parse(node.get("ts").asText()), json);
    }
}
