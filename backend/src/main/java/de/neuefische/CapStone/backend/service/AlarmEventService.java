package de.neuefische.CapStone.backend.service;

import de.neuefische.CapStone.backend.model.AlarmEntity;
import de.neuefische.CapStone.backend.model.AlarmEventEntity;
import de.neuefische.CapStone.backend.repo.AlarmEventRepository;
import de.neuefische.CapStone.backend.repo.AlarmRepository;
import de.neuefische.CapStone.backend.schedulingTask.AlarmTaskDefinition;
import de.neuefische.CapStone.backend.schedulingTask.PauseAlarmService;
import de.neuefische.CapStone.backend.schedulingTask.ScheduleAlarmService;
import de.neuefische.CapStone.backend.schedulingTask.TaskSchedulingService;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class AlarmEventService {

    private final AlarmEventRepository alarmEventRepository;
    private final CronService cronService;
    private final ScheduleAlarmService scheduleService;
    private final TaskSchedulingService taskSchedulingService;
    private final PauseAlarmService pauseAlarmService;
    private final AlarmRepository alarmRepository;

    public AlarmEventService(AlarmEventRepository alarmEventRepository, CronService cronService, ScheduleAlarmService scheduleService, TaskSchedulingService taskSchedulingService, PauseAlarmService pauseAlarmService, AlarmRepository alarmRepository) {
        this.alarmEventRepository = alarmEventRepository;
        this.cronService = cronService;
        this.scheduleService = scheduleService;
        this.taskSchedulingService = taskSchedulingService;
        this.pauseAlarmService = pauseAlarmService;
        this.alarmRepository = alarmRepository;
    }

    public AlarmEventEntity setEvent(AlarmEventEntity alarmEventEntity, AlarmEntity alarmEntity) {
        alarmEventEntity.setAlarmEntity(alarmEntity);
        alarmEventEntity.setEvent(true);
        //AlarmEventEntity newAlarmEventEntity = alarmEventRepository.saveAndFlush(alarmEventEntity);
        alarmEntity.setEvent(alarmEventEntity);
        alarmRepository.save(alarmEntity);
        if(!alarmEventEntity.isEvent()){
            return alarmEventEntity;
        }
        manageEventStart(alarmEventEntity);
        manageEventStop(alarmEventEntity);
        return alarmEventEntity;
    }

    public List<AlarmEventEntity> getAllEvents() {
        return alarmEventRepository.findAll();
    }

    public void manageEventStart(AlarmEventEntity alarmEventEntity) {
        if(alarmEventEntity.isDaily()) {
            String dailyCronStart = cronService.convertToDailyCron(alarmEventEntity.getDate());
            setAlarmTimer(alarmEventEntity, dailyCronStart, "turnOnAlarm");
        } else {
            String oneTimeAlarmCron = cronService.convertDateToCron(alarmEventEntity.getDate());
            setAlarmTimer(alarmEventEntity, oneTimeAlarmCron, "turnOnAlarm");
        }
    }

    public void manageEventStop(AlarmEventEntity alarmEventEntity) {
        if(alarmEventEntity.isDaily()) {
            String dailyCronStart = cronService.convertToDailyCron(alarmEventEntity.getDate().plus(5, ChronoUnit.MINUTES));
            setAlarmTimer(alarmEventEntity, dailyCronStart, "turnOffAlarm");
        } else {
            String oneTimeAlarmCron = cronService.convertDateToCron(alarmEventEntity.getDate().plus(5, ChronoUnit.MINUTES));
            setAlarmTimer(alarmEventEntity, oneTimeAlarmCron, "turnOffAlarm");
        }
    }

    private void setAlarmTimer(AlarmEventEntity alarmEventEntity, String alarmCron, String command) {
        if(command.equalsIgnoreCase("turnOnAlarm")){
            AlarmTaskDefinition alarmTaskDefinition = AlarmTaskDefinition.builder()
                    .actionType(command)
                    .cronExpression(alarmCron)
                    .alarmEventEntity(alarmEventEntity)
                    .date(alarmEventEntity.getDate()).build();
            scheduleService.setTaskDefinition(alarmTaskDefinition);
            taskSchedulingService.scheduleATask(alarmEventEntity.getId().toString()+"PLAY", scheduleService, alarmCron);
        } else {
            AlarmTaskDefinition alarmTaskDefinition = AlarmTaskDefinition.builder()
                    .actionType(command)
                    .cronExpression(alarmCron)
                    .alarmEventEntity(alarmEventEntity)
                    .date(alarmEventEntity.getDate()).build();
            pauseAlarmService.setTaskDefinition(alarmTaskDefinition);
            taskSchedulingService.scheduleATask(alarmEventEntity.getId()+"PAUSE", pauseAlarmService, alarmCron);
        }

    }

    public List<AlarmEventEntity> findAllAlarmEventsByAlarmDevice(AlarmEntity alarmEntity) {
        return alarmEventRepository.findAlarmEventEntitiesByAlarmEntity(alarmEntity);
    }

    public AlarmEventEntity activateAlarmEvent(AlarmEventEntity alarmEventEntity) {
        if(alarmEventEntity.getId()==null){
            throw new IllegalArgumentException("Id is null!");
        }
        Optional<AlarmEventEntity> alarmEventEntityOptional = alarmEventRepository.findAlarmEventEntityById(alarmEventEntity.getId());
        if(alarmEventEntityOptional.isEmpty()){
            throw new EntityNotFoundException("alarmEvent wasnt found!");
        }
        AlarmEventEntity newAlarmEntity = alarmEventEntityOptional.get();
        newAlarmEntity.setDate(alarmEventEntity.getDate());
        newAlarmEntity.setEvent(true);
        alarmEventRepository.save(newAlarmEntity);
        manageEventStart(newAlarmEntity);
        manageEventStop(newAlarmEntity);
        return newAlarmEntity;
    }

    public AlarmEventEntity deactivateAlarmEvent(AlarmEventEntity alarmEvent) {
        if(alarmEvent.getId()==null){
            throw new IllegalArgumentException("Id is null!");
        }
        Optional<AlarmEventEntity> alarmEventEntityOptional = alarmEventRepository.findAlarmEventEntityById(alarmEvent.getId());
        if(alarmEventEntityOptional.isEmpty()){
            throw new EntityNotFoundException("alarmEvent wasnt found!");
        }
        AlarmEventEntity alarmEventEntity = alarmEventEntityOptional.get();
        alarmEventEntity.setEvent(false);
        taskSchedulingService.removeScheduledTask(alarmEventEntity.getId()+"PLAY");
        taskSchedulingService.removeScheduledTask(alarmEventEntity.getId()+"PAUSE");
        return alarmEventRepository.save(alarmEventEntity);
    }

    public AlarmEventEntity deleteAlarmEvent(AlarmEventEntity alarmEventEntity) {
        alarmEventRepository.delete(alarmEventEntity);
        return alarmEventEntity;
    }

    public AlarmEventEntity findAlarmEventById(String id) {
        Optional<AlarmEventEntity> alarmEventEntityOptional = alarmEventRepository.findAlarmEventEntityById(Long.parseLong(id));
        if(alarmEventEntityOptional.isEmpty()) {
            throw new EntityNotFoundException("AlarmEventEntity couldn't be found! Id incorrect");
        }
        return alarmEventEntityOptional.get();
    }
}
