package org.example.application.scheduler;

import org.example.application.controller.Control;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;

public class WeatherScheduler {
    private final Scheduler scheduler;
    private final JobDetail job;

    public WeatherScheduler(String[] cities, Control controller) throws SchedulerException {
        this.scheduler = StdSchedulerFactory.getDefaultScheduler();

        JobDataMap jobData = getJobDataMap(cities, controller);

        this.job = newJob(WeatherJob.class).withIdentity("weatherJob").usingJobData(jobData).build();

        Trigger hourlyTrigger = getHourlyTrigger();
        scheduler.scheduleJob(job, hourlyTrigger);
    }

    private static JobDataMap getJobDataMap(String[] cities, Control controller) {
        JobDataMap jobData = new JobDataMap();
        jobData.put("cities", cities);
        jobData.put("controller", controller);
        return jobData;
    }

    private Trigger getHourlyTrigger() {
        return newTrigger().withIdentity("hourlyTrigger")
                .withSchedule(
                        cronSchedule("0 0 * * * ?")
                )
                .forJob(job).build();
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