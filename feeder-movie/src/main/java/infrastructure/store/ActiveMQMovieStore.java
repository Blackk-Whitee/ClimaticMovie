package infrastructure.store;
import com.google.gson.Gson;
import domain.models.Movie;
import jakarta.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;

public class ActiveMQMovieStore implements MovieStore {
    private final String url;
    private final String topic;

    public ActiveMQMovieStore(String url, String topic) {
        this.url = url;
        this.topic = topic;
    }

    @Override
    public void save(Movie movie) {
        ConnectionFactory factory = new ActiveMQConnectionFactory(url);
        try (Connection connection = factory.createConnection()) {
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Topic topic = session.createTopic(this.topic);
            MessageProducer producer = session.createProducer(topic);
            producer.send(session.createTextMessage(new Gson().toJson(movie)));
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }
}
