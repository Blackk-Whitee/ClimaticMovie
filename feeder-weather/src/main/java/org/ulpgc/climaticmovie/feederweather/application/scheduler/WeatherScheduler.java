package org.ulpgc.climaticmovie.feederweather.application.scheduler;

import org.ulpgc.climaticmovie.feederweather.application.controller.Control;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.List;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.repeatHourlyForever;
import static org.quartz.TriggerBuilder.newTrigger;

public class WeatherScheduler {
    private final Scheduler scheduler;

    public WeatherScheduler(List<String> cities, Control controller) throws SchedulerException {
        this.scheduler = StdSchedulerFactory.getDefaultScheduler();

        JobDataMap jobData = new JobDataMap();
        jobData.put("cities", cities);
        jobData.put("controller", controller);
        JobDetail job = newJob(WeatherJob.class).withIdentity("weatherJob").usingJobData(jobData).build();

        // Primera ejecución inmediata y después cada hora
        Trigger hourlyTrigger = newTrigger().withIdentity("hourlyTrigger")
                .startNow()
                .withSchedule(repeatHourlyForever())
                .build();
        scheduler.scheduleJob(job, hourlyTrigger);
    }

    public void start() throws SchedulerException {
        scheduler.start();
    }

    public static class WeatherJob implements Job {
        @Override
        @SuppressWarnings("unchecked")
        public void execute(JobExecutionContext context) {
            JobDataMap data = context.getJobDetail().getJobDataMap();
            List<String> cities = (List<String>) data.get("cities");
            Control controller = (Control) data.get("controller");

            for (String city : cities) {
                controller.fetchAndSaveWeather(city);
            }
        }
    }
}
