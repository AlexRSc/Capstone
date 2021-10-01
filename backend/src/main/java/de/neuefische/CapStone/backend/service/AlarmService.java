package de.neuefische.CapStone.backend.service;

import de.neuefische.CapStone.backend.api.AlarmEvent;
import de.neuefische.CapStone.backend.model.AlarmEntity;
import de.neuefische.CapStone.backend.model.AlarmEventEntity;
import de.neuefische.CapStone.backend.model.HubEntity;
import de.neuefische.CapStone.backend.repo.AlarmEventRepository;
import de.neuefische.CapStone.backend.repo.AlarmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
public class AlarmService {

    private final AlarmRepository alarmRepository;
    private final OpenHabService openHabService;
    private final AlarmEventRepository alarmEventRepository;

    @Autowired
    public AlarmService(AlarmRepository alarmRepository, OpenHabService openHabService, AlarmEventRepository alarmEventRepository) {
        this.alarmRepository = alarmRepository;
        this.openHabService = openHabService;
        this.alarmEventRepository = alarmEventRepository;
    }

    public AlarmEntity findAlarmEntity(String uid) {
        Optional<AlarmEntity> alarmEntityOptional = alarmRepository.findAlarmEntityByAlarmDevice_Uid(uid);
        if (alarmEntityOptional.isEmpty()) {
            throw new EntityNotFoundException("Entity with ID " + uid + " not found");
        }
        return alarmEntityOptional.get();
    }

    public AlarmEntity createAlarmDevice(AlarmEntity alarmEntity, HubEntity hubEntity) {
        alarmEntity.setHubEntity(hubEntity);
        return alarmRepository.save(alarmEntity);
    }

    public List<AlarmEntity> findAllAlarmsByUsername(String userName) {
        return alarmRepository.findAllByAlarmDevice_UserName(userName);
    }

    public AlarmEntity setAlarmEntityVolume(AlarmEntity alarmEntity, String volume) {
        alarmEntity.getAlarmStates().setVolume(volume);
        openHabService.setAlarmVolume(alarmEntity);
        return alarmRepository.save(alarmEntity);
    }

    public AlarmEntity turnOnAlarmDevice(AlarmEntity alarmEntity) {
        alarmEntity.getAlarmStates().setOnOff(true);
        AlarmEventEntity alarmEventEntity = AlarmEventEntity.builder()
                        .alarmEntity(alarmEntity).build();
        openHabService.turnAlarmOn(alarmEventEntity);
        return alarmRepository.save(alarmEntity);
    }

    public AlarmEntity turnOffAlarmDevice(AlarmEntity alarmEntity) {
        alarmEntity.getAlarmStates().setOnOff(false);
        AlarmEventEntity alarmEventEntity = AlarmEventEntity.builder()
                .alarmEntity(alarmEntity).build();
        openHabService.turnAlarmOff(alarmEventEntity);
        return alarmRepository.save(alarmEntity);
    }

    public AlarmEntity deleteAlarmDevice(AlarmEntity alarmEntity) {
        alarmRepository.delete(alarmEntity);
        return alarmEntity;
    }
}
