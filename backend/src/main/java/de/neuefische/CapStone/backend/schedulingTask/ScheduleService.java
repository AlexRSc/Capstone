package de.neuefische.CapStone.backend.schedulingTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.config.ScheduledTask;
import org.springframework.stereotype.Service;

@Service
public class ScheduleService implements Runnable{
    private TaskDefinitionTest taskDefinition;
    private static final Logger log = LoggerFactory.getLogger(ScheduledTask.class);

    @Override
    public void run() {
        log.info("Running action:" + taskDefinition.getActionType());
        log.info("With Data: " + taskDefinition.getDate());
        log.info("With User: " + taskDefinition.getDevice().getUserName());
        log.info("With Data: " + taskDefinition.getDevice().getDeviceName());
    }

    public TaskDefinitionTest getTaskDefinition() {
        return taskDefinition;
    }

    public void setTaskDefinition(TaskDefinitionTest taskDefinition) {
        this.taskDefinition = taskDefinition;
    }
}
