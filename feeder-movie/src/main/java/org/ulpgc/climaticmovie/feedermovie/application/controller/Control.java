package org.ulpgc.climaticmovie.feedermovie.application.controller;

import org.ulpgc.climaticmovie.feedermovie.domain.models.Movie;
import org.ulpgc.climaticmovie.feedermovie.infrastructure.api.MovieProvider;
import org.ulpgc.climaticmovie.feedermovie.infrastructure.store.MovieStore;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Control {
    private final MovieProvider provider;
    private final MovieStore store;

    public Control(MovieProvider provider, MovieStore store) {
        this.provider = provider;
        this.store = store;
    }

    public void startDailyFetch() throws SchedulerException {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("control", this);

        JobDetail job = JobBuilder.newJob(FetchDataJob.class)
                .withIdentity("dailyFetchJob", "group1")
                .usingJobData(jobDataMap)
                .build();
        // Primera ejecución inmediata y después cada 24 horas
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("dailyTrigger", "group1")
                .startNow()
                .withSchedule(SimpleScheduleBuilder.repeatHourlyForever(24))
                .build();
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.start();
        scheduler.scheduleJob(job, trigger);
    }

    public int fetchAndSend() {
        List<Movie> movies = provider.provide();
        Set<String> sentTitles = new HashSet<>();
        movies.stream()
                .filter(movie -> sentTitles.add(movie.getTitle()))
                .forEach(store::save);
        System.out.println("Películas enviadas: " + sentTitles.size());
        return sentTitles.size();
    }

    public static class FetchDataJob implements Job {
        @Override
        public void execute(JobExecutionContext context) {
            Control control = (Control) context.getJobDetail().getJobDataMap().get("control");
            control.fetchAndSend();
        }
    }
}
