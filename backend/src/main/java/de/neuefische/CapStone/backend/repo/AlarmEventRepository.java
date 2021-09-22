package de.neuefische.CapStone.backend.repo;

import de.neuefische.CapStone.backend.model.AlarmEntity;
import de.neuefische.CapStone.backend.model.AlarmEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AlarmEventRepository extends JpaRepository<AlarmEventEntity, Long> {
    List<AlarmEventEntity> findAlarmEventEntitiesByAlarmEntity(AlarmEntity alarmEntity);

    Optional<AlarmEventEntity> findAlarmEventEntityByAlarmEntity_Id(long id);
}
