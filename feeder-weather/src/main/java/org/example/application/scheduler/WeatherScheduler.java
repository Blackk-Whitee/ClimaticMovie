package org.example.application.scheduler;

import org.example.application.controller.Control;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import java.util.Set;
import static org.quartz.CronScheduleBuilder.dailyAtHourAndMinute;
import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;

public class WeatherScheduler {
    private final Scheduler scheduler;
    private final JobDetail job;

    public WeatherScheduler(String[] cities, Control controller) throws SchedulerException {
        this.scheduler = StdSchedulerFactory.getDefaultScheduler();

        JobDataMap jobData = getJobDataMap(cities, controller);

        this.job = newJob(WeatherJob.class).withIdentity("weatherJob").usingJobData(jobData).build();

        Trigger morningTrigger = getMorningTrigger();
        Trigger eveningTrigger = getEveningTrigger();
        scheduler.scheduleJob(job, Set.of(morningTrigger, eveningTrigger), true);
    }

    private static JobDataMap getJobDataMap(String[] cities, Control controller) {
        JobDataMap jobData = new JobDataMap();
        jobData.put("cities", cities);
        jobData.put("controller", controller);
        return jobData;
    }

    private Trigger getEveningTrigger() {
        return newTrigger()
                .withIdentity("eveningTrigger")
                .withSchedule(dailyAtHourAndMinute(19, 51)) // 7:00 PM
                .forJob(job)
                .build();
    }

    private Trigger getMorningTrigger() {
        return newTrigger()
                .withIdentity("morningTrigger")
                .withSchedule(dailyAtHourAndMinute(7, 0)) // 7:00 AM
                .forJob(job)
                .build();
    }

    public void start() throws SchedulerException {
        scheduler.start();
    }

    public static class WeatherJob implements Job {
        @Override
        public void execute(JobExecutionContext context) {
            JobDataMap data = context.getJobDetail().getJobDataMap();
            String[] cities = (String[]) data.get("cities");
            Control controller = (Control) data.get("controller");

            for (String city : cities) {
                controller.fetchAndSaveWeather(city);
            }
        }
    }
}