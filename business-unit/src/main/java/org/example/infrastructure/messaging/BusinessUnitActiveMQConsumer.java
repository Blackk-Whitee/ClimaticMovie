package org.example.infrastructure.messaging;

import org.example.aplication.controller.Control;

import javax.jms.*;

import java.util.List;

public class BusinessUnitActiveMQConsumer {
    private final ActiveMQRealTimeConsumer consumer;
    private final Control control;

    public BusinessUnitActiveMQConsumer(String brokerUrl, Control control) {
        this.control = control;

        // Listener que diferencia por topic
        MessageListener listener = message -> {
            try {
                if (!(message instanceof TextMessage)) {
                    System.err.println("Mensaje no es de texto, se ignora");
                    return;
                }
                TextMessage textMessage = (TextMessage) message;
                String json = textMessage.getText();

                Destination destination = message.getJMSDestination();
                if (destination instanceof Topic) {
                    String topicName = ((Topic) destination).getTopicName();
                    switch (topicName) {
                        case "Weather": // Clima
                            control.updateWeatherData(json);
                            break;
                        case "movies.Trending": // Películas
                            control.updateMoviesData(json);
                            break;
                        default:
                            System.err.println("Topic desconocido: " + topicName);
                    }
                }
            } catch (JMSException e) {
                System.err.println("Error procesando mensaje: " + e.getMessage());
            }
        };

        List<String> topics = List.of("Weather", "movies.Trending");
        this.consumer = new ActiveMQRealTimeConsumer(brokerUrl, "business-unit", topics, listener);
    }

    public void start() {
        consumer.start();
    }

    public void shutdown() {
        consumer.shutdown();
    }
}
