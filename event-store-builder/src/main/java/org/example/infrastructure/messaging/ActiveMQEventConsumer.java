package org.example.infrastructure.messaging;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class ActiveMQEventConsumer {
    private final String brokerUrl;
    private final String topicName;
    private final MessageListener listener;

    public ActiveMQEventConsumer(String brokerUrl, String topicName, MessageListener listener) {
        this.brokerUrl = brokerUrl;
        this.topicName = topicName;
        this.listener = listener;
    }

    public void listen() {
        try {
            Connection connection = getConnection();

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Topic topic = session.createTopic(topicName);
            MessageConsumer consumer = session.createDurableConsumer(topic, "weather-subscription");
            consumer.setMessageListener(listener);

        } catch (JMSException e) {
            throw new RuntimeException("Error al configurar ActiveMQ", e);
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