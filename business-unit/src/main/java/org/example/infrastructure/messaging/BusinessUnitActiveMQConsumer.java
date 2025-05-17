package org.example.infrastructure.messaging;

import org.example.aplication.controller.Control;
import org.example.domain.services.EventAccumulator;

import javax.jms.*;
import java.util.List;
import java.util.concurrent.*;

public class BusinessUnitActiveMQConsumer {
    private final ActiveMQRealTimeConsumer consumer;
    private final Control control;
    private final EventAccumulator accumulator = new EventAccumulator();

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> scheduledProcessing;

    // Tiempo de inactividad antes de procesar (ms)
    private static final long IDLE_TIMEOUT_MS = 5000;

    public BusinessUnitActiveMQConsumer(String brokerUrl, Control control) {
        this.control = control;

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
                    System.out.println("[INFO] Mensaje recibido en topic: " + topicName);
                    System.out.println("[DEBUG] Contenido: " + json);

                    switch (topicName) {
                        case "Weather":
                            accumulator.addWeatherEvent(json);
                            break;
                        case "movies.Trending":
                            accumulator.addMovieEvent(json);
                            break;
                        default:
                            System.err.println("Topic desconocido: " + topicName);
                    }

                    resetIdleTimer(); // Reinicia temporizador
                }

                message.acknowledge();
            } catch (JMSException e) {
                System.err.println("Error procesando mensaje: " + e.getMessage());
            }
        };

        List<String> topics = List.of("Weather", "movies.Trending");
        this.consumer = new ActiveMQRealTimeConsumer(brokerUrl, "business-unit", topics, listener);
    }

    private void resetIdleTimer() {
        if (scheduledProcessing != null && !scheduledProcessing.isDone()) {
            scheduledProcessing.cancel(false);
        }

        scheduledProcessing = scheduler.schedule(() -> {
            System.out.println("[INFO] Tiempo de inactividad alcanzado. Procesando lote...");
            control.processHistoricalData(
                    accumulator.getWeatherEvents(),
                    accumulator.getMovieEvents()
            );
            accumulator.clear();
        }, IDLE_TIMEOUT_MS, TimeUnit.MILLISECONDS);
    }

    public void start() {
        consumer.start();
    }

    public void shutdown() {
        consumer.shutdown();
        scheduler.shutdown();
    }
}
