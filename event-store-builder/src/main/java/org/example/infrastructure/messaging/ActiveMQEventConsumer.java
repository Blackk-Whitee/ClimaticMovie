package org.example.infrastructure.messaging;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.List;

public class ActiveMQEventConsumer {
    private final String brokerUrl;
    private final List<String> topicNames;
    private final MessageListener listener;

    private Connection connection;
    private Session session;

    public ActiveMQEventConsumer(String brokerUrl, List<String> topicNames, MessageListener listener) {
        this.brokerUrl = brokerUrl;
        this.topicNames = topicNames;
        this.listener = listener;
    }

    public void listen() {
        try {
            this.connection = getConnection();
            this.session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);

            for (String topicName : topicNames) {
                setListener(topicName);
            }

        } catch (JMSException e) {
            throw new RuntimeException("Error al configurar ActiveMQ", e);
        }
    }

    private void setListener(String topicName) throws JMSException {
        Topic topic = session.createTopic(topicName);
        MessageConsumer consumer = session.createDurableConsumer(topic, "subscription-" + topicName);
        consumer.setMessageListener(listener);
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
