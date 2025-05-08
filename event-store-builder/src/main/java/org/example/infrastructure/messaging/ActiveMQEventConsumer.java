package org.example.infrastructure.messaging;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.List;

public class ActiveMQEventConsumer {
    private final String brokerUrl;
    private final List<String> topicNames;
    private final MessageListener listener;

    public ActiveMQEventConsumer(String brokerUrl, List<String> topicNames, MessageListener listener) {
        this.brokerUrl = brokerUrl;
        this.topicNames = topicNames;
        this.listener = listener;
    }

    public void listen() {
        try {
            Connection connection = getConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            for (String topicName : topicNames) {
                Topic topic = session.createTopic(topicName);
                MessageConsumer consumer = session.createDurableConsumer(topic, "subscription-" + topicName);
                consumer.setMessageListener(listener);
            }

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
