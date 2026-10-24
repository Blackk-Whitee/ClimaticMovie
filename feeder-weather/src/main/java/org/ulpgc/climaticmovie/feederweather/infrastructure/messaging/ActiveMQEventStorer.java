package org.ulpgc.climaticmovie.feederweather.infrastructure.messaging;

import com.google.gson.JsonObject;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.ulpgc.climaticmovie.feederweather.domain.models.Weather;

import javax.jms.*;

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
            producer.send(session.createTextMessage(eventJson));
            System.out.println("[PUBLISHER] Evento enviado al topic '" + topicName + "': " + eventJson);
        } catch (JMSException e) {
            System.err.println("Error al publicar evento: " + e.getMessage());
        }
    }

    private String buildEventJson(Weather weather, String source) {
        JsonObject event = new JsonObject();
        event.addProperty("topic", topicName);
        event.addProperty("ts", weather.getTimestamp().toString());
        event.addProperty("ss", source);
        event.addProperty("city", weather.getCity());
        event.addProperty("temperature", weather.getTemperature());
        event.addProperty("humidity", weather.getHumidity());
        event.addProperty("condition", weather.getCondition());
        event.addProperty("windSpeed", weather.getWindSpeed());
        return event.toString();
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
