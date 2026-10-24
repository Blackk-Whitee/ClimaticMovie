package org.ulpgc.climaticmovie.feedermovie.infrastructure.store;

import com.google.gson.Gson;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.ulpgc.climaticmovie.feedermovie.domain.models.Movie;

import javax.jms.*;

public class ActiveMQMovieStore implements MovieStore {
    private final Gson gson = new Gson();
    private final Connection connection;
    private final Session session;
    private final MessageProducer producer;

    public ActiveMQMovieStore(String url, String topicName) {
        try {
            this.connection = new ActiveMQConnectionFactory(url).createConnection();
            this.connection.start();
            this.session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Topic topic = session.createTopic(topicName);
            this.producer = session.createProducer(topic);
        } catch (JMSException e) {
            throw new RuntimeException("Error al inicializar ActiveMQ", e);
        }
    }

    @Override
    public synchronized void save(Movie movie) {
        try {
            producer.send(session.createTextMessage(gson.toJson(movie)));
        } catch (JMSException e) {
            System.err.println("Error al publicar la película '" + movie.getTitle() + "': " + e.getMessage());
        }
    }

    public void close() {
        try {
            producer.close();
            session.close();
            connection.close();
        } catch (JMSException e) {
            System.err.println("Error al cerrar ActiveMQ: " + e.getMessage());
        }
    }
}
