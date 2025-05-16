package infrastructure;
import core.Movie;
import infrastructure.api.MovieProvider;
import infrastructure.store.MovieStore;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.List;

public class Control {
    private final MovieProvider provider;
    private final MovieStore store;

    public Control(MovieProvider provider, MovieStore store) {
        this.provider = provider;
        this.store = store;
    }

    public void startDailyFetch() throws SchedulerException {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("provider", provider);
        jobDataMap.put("store", store);

        JobDetail job = JobBuilder.newJob(FetchDataJob.class)
                .withIdentity("dailyFetchJob", "group1")
                .usingJobData(jobDataMap)
                .build();
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("dailyTrigger", "group1")
                .withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(23, 17))
                .build();
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.start(); scheduler.scheduleJob(job, trigger);
    }

    public static class FetchDataJob implements Job {
        @Override
        public void execute(JobExecutionContext context) {
            // Obtiene provider y store del JobDataMap
            JobDataMap dataMap = context.getJobDetail().getJobDataMap();
            MovieProvider provider = (MovieProvider) dataMap.get("provider");
            MovieStore store = (MovieStore) dataMap.get("store");

            // Ejecuta la lógica
            List<Movie> movies = provider.provide();
            movies.forEach(store::save);
            System.out.println(movies);
        }
    }
}