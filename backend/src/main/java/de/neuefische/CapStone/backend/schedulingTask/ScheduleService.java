package de.neuefische.CapStone.backend.schedulingTask;

import de.neuefische.CapStone.backend.service.OpenHabService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.config.ScheduledTask;
import org.springframework.stereotype.Service;

@Service
public class ScheduleService implements Runnable{
    private TaskDefinition taskDefinition;
    private final OpenHabService openHabService;
    private static final Logger log = LoggerFactory.getLogger(ScheduledTask.class);

    @Autowired
    public ScheduleService(OpenHabService openHabService) {
        this.openHabService = openHabService;
    }


    @Override
    public void run() {
        log.info("Running action: " + taskDefinition.getActionType());
        log.info("With Data: " + taskDefinition.getDate());
        log.info("With User: " + taskDefinition.getCoffeeEntity().getDevice().getUserName());
        log.info("With Data: " + taskDefinition.getCoffeeEntity().getDevice().getDeviceName());
        openHabService.turnCoffeeMachineOn(taskDefinition.getCoffeeEntity());
    }

    public TaskDefinition getTaskDefinition() {
        return taskDefinition;
    }

    public void setTaskDefinition(TaskDefinition taskDefinition) {
        this.taskDefinition = taskDefinition;
    }
}
