package de.neuefische.CapStone.backend.schedulingTask;

import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

@Service
public class ScheduleTaskService {

    private TaskScheduler taskScheduler;

    public ScheduleTaskService(TaskScheduler taskScheduler) {
        this.taskScheduler = taskScheduler;
    }

    public void addTaskTOScheduler(int id, Runnable task) {

    }
}
