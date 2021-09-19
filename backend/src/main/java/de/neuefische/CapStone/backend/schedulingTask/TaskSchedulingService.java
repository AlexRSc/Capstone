package de.neuefische.CapStone.backend.schedulingTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.config.ScheduledTask;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ScheduledFuture;

@Service
public class TaskSchedulingService {

    private final TaskScheduler taskScheduler;
    private static final Logger log = LoggerFactory.getLogger(ScheduledTask.class);
    Map<String, ScheduledFuture<?>> jobsMap = new HashMap<>();

    @Autowired
    public TaskSchedulingService(TaskScheduler taskScheduler) {
        this.taskScheduler = taskScheduler;
    }

    public void scheduleATask(String jobId, Runnable tasklet, String cronExpression) {
        log.info("Scheduling task with job id: " + jobId + "and cron expression " + cronExpression);
        ScheduledFuture<?> scheduledTask = taskScheduler.schedule(tasklet, new CronTrigger(cronExpression, TimeZone.getTimeZone(TimeZone.getDefault().getID())));
        jobsMap.put(jobId, scheduledTask);
    }

    public void removeScheduledTask(String jobID) {
        ScheduledFuture<?> scheduledTask = jobsMap.get(jobID);
        if (scheduledTask != null) {
            scheduledTask.cancel(true);
            jobsMap.put(jobID, null);
        }
    }
}
