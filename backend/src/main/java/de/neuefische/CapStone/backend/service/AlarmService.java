package de.neuefische.CapStone.backend.service;

import de.neuefische.CapStone.backend.model.AlarmEntity;
import de.neuefische.CapStone.backend.model.HubEntity;
import de.neuefische.CapStone.backend.repo.AlarmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class AlarmService {

    @Autowired
    private final AlarmRepository alarmRepository;

    public AlarmService(AlarmRepository alarmRepository) {
        this.alarmRepository = alarmRepository;
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
}
