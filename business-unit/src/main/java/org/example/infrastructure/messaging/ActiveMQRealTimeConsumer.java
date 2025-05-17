package org.example.infrastructure.messaging;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.List;

public class ActiveMQRealTimeConsumer {
    private final String brokerUrl;
    private final String clientID;
    private final List<String> topicNames;
    private final MessageListener listener;

    private Connection connection;
    private Session session;

    public ActiveMQRealTimeConsumer(String brokerUrl, String clientID, List<String> topicNames, MessageListener listener) {
        this.brokerUrl = brokerUrl;
        this.clientID = clientID;
        this.topicNames = topicNames;
        this.listener = listener;
    }

    public void start() {
        try {
            ConnectionFactory factory = new ActiveMQConnectionFactory(brokerUrl);
            connection = factory.createConnection();
            connection.setClientID(clientID);
            connection.start();

            session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);

            for (String topicName : topicNames) {
                Topic topic = session.createTopic(topicName);
                MessageConsumer consumer = session.createDurableConsumer(topic, "subscription-" + topicName + "-" + clientID);
                consumer.setMessageListener(listener);
            }
            System.out.println("Suscripción duradera iniciada para clientID: " + clientID);
        } catch (JMSException e) {
            throw new RuntimeException("Error configurando ActiveMQ", e);
        }
    }

    public void shutdown() {
        try {
            if (session != null) session.close();
            if (connection != null) connection.close();
            System.out.println("Conexión ActiveMQ cerrada para clientID: " + clientID);
        } catch (JMSException e) {
            System.err.println("Error cerrando conexión: " + e.getMessage());
        }
    }
}
