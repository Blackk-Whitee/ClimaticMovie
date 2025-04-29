package infrastructure.scheduler;
import core.usecases.FetchGenres;
import core.usecases.FetchMovies;
import infrastructure.api.GenreApiClient;
import infrastructure.api.MovieApiClient;
import infrastructure.db.SQLiteConnection;
import infrastructure.db.SQLiteRepositoryInterface;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class Control {
    private final String apiKey;
    private final String dbPath; // Cambio: Ahora almacenamos el path directamente

    public Control(String apiKey, String dbPath) { // Cambio: Recibe el path como String
        this.apiKey = apiKey;
        this.dbPath = dbPath;
    }

    public void startDailyFetch() throws SchedulerException {
        JobDetail job = JobBuilder.newJob(FetchDataJob.class)
                .withIdentity("dailyFetchJob", "group1")
                .usingJobData("apiKey", apiKey)
                .usingJobData("dbPath", dbPath) // Pasamos el String directamente
                .build();
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("dailyTrigger", "group1")
                .withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(11, 35))
                .build();
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.start(); scheduler.scheduleJob(job, trigger);
    }

    public static class FetchDataJob implements Job {
        @Override
        public void execute(JobExecutionContext context) {
            JobDataMap data = context.getJobDetail().getJobDataMap();
            SQLiteRepositoryInterface repo = new SQLiteConnection(data.getString("dbPath"));
            new FetchGenres(new GenreApiClient(data.getString("apiKey")), repo).execute();
            new FetchMovies(new MovieApiClient(data.getString("apiKey")), repo).execute(20);
        }
    }
}