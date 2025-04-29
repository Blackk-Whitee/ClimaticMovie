import infrastructure.db.SQLiteConnection;
import infrastructure.scheduler.Control;
import org.quartz.SchedulerException;

public class Main {
    public static void main(String[] args) throws SchedulerException {
        SQLiteConnection db = new SQLiteConnection(args[1]);
        db.initializeDatabase();
        Control scheduler = new Control(args[0], args[1]);
        scheduler.startDailyFetch();
        System.out.println("Las actualizaciones se ejecutarán diariamente a las 15:00.");
    }
}