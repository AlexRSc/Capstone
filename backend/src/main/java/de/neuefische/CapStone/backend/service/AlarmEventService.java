package de.neuefische.CapStone.backend.service;

import de.neuefische.CapStone.backend.model.AlarmEntity;
import de.neuefische.CapStone.backend.model.AlarmEventEntity;
import de.neuefische.CapStone.backend.repo.AlarmEventRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlarmEventService {

    private final AlarmEventRepository alarmEventRepository;
    private final AlarmService alarmService;

    public AlarmEventService(AlarmEventRepository alarmEventRepository, AlarmService alarmService) {
        this.alarmEventRepository = alarmEventRepository;
        this.alarmService = alarmService;
    }

    public AlarmEventEntity setEvent(AlarmEventEntity alarmEventEntity, AlarmEntity alarmEntity) {
        alarmEventEntity.setAlarmEntity(alarmEntity);
        return alarmEventRepository.save(alarmEventEntity);
    }

    public List<AlarmEventEntity> findAllAlarmEventsByAlarmDevice(AlarmEntity alarmEntity) {
        return alarmEventRepository.findAlarmEventEntitiesByAlarmEntity(alarmEntity);
    }
}
