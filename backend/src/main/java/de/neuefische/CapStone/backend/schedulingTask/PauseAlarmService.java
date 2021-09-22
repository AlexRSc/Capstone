package de.neuefische.CapStone.backend.schedulingTask;

import de.neuefische.CapStone.backend.service.OpenHabService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.config.ScheduledTask;
import org.springframework.stereotype.Service;

@Service
public class PauseAlarmService implements Runnable {
    private AlarmTaskDefinition taskDefinition;
    private final OpenHabService openHabService;
    private static final Logger log = LoggerFactory.getLogger(ScheduledTask.class);

    @Autowired
    public PauseAlarmService(OpenHabService openHabService) {
        this.openHabService = openHabService;
    }


    @Override
    public void run() {
        log.info("Action: " + taskDefinition.getActionType());
        log.info("for user: " + taskDefinition.getAlarmEventEntity().getAlarmEntity().getAlarmDevice().getUserName());
        if(taskDefinition.getActionType().equalsIgnoreCase("turnOnAlarm")){
            openHabService.turnAlarmOn(taskDefinition.getAlarmEventEntity());
        }
        if(taskDefinition.getActionType().equalsIgnoreCase("turnOffAlarm")){
            openHabService.turnAlarmOff(taskDefinition.getAlarmEventEntity());
        }

    }

    public AlarmTaskDefinition getTaskDefinition() {
        return taskDefinition;
    }

    public void setTaskDefinition(AlarmTaskDefinition taskDefinition) {
        this.taskDefinition = taskDefinition;
    }
}