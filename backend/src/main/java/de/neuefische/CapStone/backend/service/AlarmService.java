package de.neuefische.CapStone.backend.service;

import de.neuefische.CapStone.backend.model.AlarmEntity;
import de.neuefische.CapStone.backend.model.HubEntity;
import de.neuefische.CapStone.backend.repo.AlarmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlarmService {

    @Autowired
    private final AlarmRepository alarmRepository;

    public AlarmService(AlarmRepository alarmRepository) {
        this.alarmRepository = alarmRepository;
    }

    public AlarmEntity createAlarmDevice(AlarmEntity alarmEntity, HubEntity hubEntity) {
        alarmEntity.setHubEntity(hubEntity);
        return alarmRepository.save(alarmEntity);
    }
}
