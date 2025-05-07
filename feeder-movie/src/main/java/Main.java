import infrastructure.store.ActiveMQMovieStore;
import infrastructure.api.TmdbMovieProvider;
import infrastructure.Control;
import org.quartz.SchedulerException;

public class Main {
    public static void main(String[] args) throws SchedulerException {
        //SQLiteConnection store = new SQLiteConnection(args[1]);
        //db.initializeDatabase();
        ActiveMQMovieStore store = new ActiveMQMovieStore(args[2], args[3]);
        TmdbMovieProvider provider = new TmdbMovieProvider(args[0]);
        Control control = new Control(provider, store);
        control.startDailyFetch();
        System.out.println("Las actualizaciones se ejecutarán diariamente a las 15:00.");
    }
}