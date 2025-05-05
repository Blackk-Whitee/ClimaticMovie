package org.example.infrastructure.messaging;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.example.domain.models.Weather;
import javax.jms.*;
import java.time.Instant;

public class ActiveMQEventStorer {
    private final String brokerUrl;
    private final String topicName;
    private Connection connection;
    private Session session;
    private MessageProducer producer;

    public ActiveMQEventStorer(String brokerUrl, String topicName) {
        this.brokerUrl = brokerUrl;
        this.topicName = topicName;
        initialize();
    }

    private void initialize() {
        try {
            ConnectionFactory factory = new ActiveMQConnectionFactory(brokerUrl);
            this.connection = factory.createConnection();
            this.connection.start();
            this.session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Topic topic = session.createTopic(topicName);
            this.producer = session.createProducer(topic);
        } catch (JMSException e) {
            throw new RuntimeException("Error al inicializar ActiveMQ", e);
        }
    }

    public void publishWeatherEvent(Weather weather, String source) {
        try {
            String eventJson = buildEventJson(weather, source);
            TextMessage message = session.createTextMessage(eventJson);
            producer.send(message);
            System.out.println("[PUBLISHER] Evento enviado al topic '" + topicName + "': " + eventJson);
        } catch (JMSException e) {
            System.err.println("Error al publicar evento: " + e.getMessage());
        }
    }

    private String buildEventJson(Weather weather, String source) {
        return String.format(
                "{\"ts\":\"%s\", \"ss\":\"%s\", \"city\":\"%s\", \"temperature\":%s, \"humidity\":%d, \"condition\":\"%s\"}",
                Instant.now().toString(),
                source,
                weather.getCity(), weather.getTemperatureAsString(), weather.getHumidity(), weather.getCondition()
        );
    }

    public void close() {
        try {
            if (producer != null) producer.close();
            if (session != null) session.close();
            if (connection != null) connection.close();
        } catch (JMSException e) {
            System.err.println("Error al cerrar recursos: " + e.getMessage());
        }
    }
}