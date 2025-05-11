package org.example.infrastructure.messaging;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.List;

public class ActiveMQEventConsumer {
    private final String brokerUrl;
    private final List<String> topicNames;
    private final MessageListener listener;

    private Connection connection; // mantener viva
    private Session session;

    public ActiveMQEventConsumer(String brokerUrl, List<String> topicNames, MessageListener listener) {
        this.brokerUrl = brokerUrl;
        this.topicNames = topicNames;
        this.listener = listener;
    }

    // En ActiveMQEventConsumer.java
    public void listen() {
        try {
            this.connection = getConnection();
            // Cambia a Session.CLIENT_ACKNOWLEDGE para control manual
            this.session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);

            for (String topicName : topicNames) {
                Topic topic = session.createTopic(topicName);
                MessageConsumer consumer = session.createDurableConsumer(topic, "subscription-" + topicName);
                consumer.setMessageListener(listener);
            }
        } catch (JMSException e) {
            throw new RuntimeException("Error al configurar ActiveMQ", e);
        }
    }

    public void shutdown() {
        try {
            if (session != null) session.close();
            if (connection != null) connection.close();
        } catch (JMSException e) {
            System.err.println("Error cerrando conexión: " + e.getMessage());
        }
    }

    private Connection getConnection() throws JMSException {
        ConnectionFactory factory = new ActiveMQConnectionFactory(brokerUrl);
        Connection connection = factory.createConnection();
        connection.setClientID("event-store-builder");
        connection.start();
        return connection;
    }
}
