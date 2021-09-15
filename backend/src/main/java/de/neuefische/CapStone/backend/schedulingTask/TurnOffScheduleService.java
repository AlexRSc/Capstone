package de.neuefische.CapStone.backend.schedulingTask;

import de.neuefische.CapStone.backend.service.OpenHabService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.config.ScheduledTask;
import org.springframework.stereotype.Service;

@Service
public class TurnOffScheduleService implements Runnable{
    private TaskDefinition taskDefinition;
    private static final Logger log = LoggerFactory.getLogger(ScheduledTask.class);
    private final OpenHabService openHabService;

    @Autowired
    public TurnOffScheduleService(OpenHabService openHabService) {
        this.openHabService = openHabService;
    }


    //this Service was already in here, but somehow, putting 2 threads at the same time overwrites them. Simple and ugly
    //way to get around that is to have a secondary service to set the turnOff thread
    @Override
    public void run() {
        log.info("Running action: " + taskDefinition.getActionType());
        log.info("With Data: " + taskDefinition.getDate());
        log.info("With User: " + taskDefinition.getCoffeeEntity().getDevice().getUserName());
        log.info("With Data: " + taskDefinition.getCoffeeEntity().getDevice().getDeviceName());
        openHabService.turnCoffeeMachineOFF(taskDefinition.getCoffeeEntity());
    }

    public TaskDefinition getTaskDefinition() {
        return taskDefinition;
    }

    public void setTaskDefinition(TaskDefinition taskDefinition) {
        this.taskDefinition = taskDefinition;
    }
}
