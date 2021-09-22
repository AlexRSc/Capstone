package de.neuefische.CapStone.backend.repo;

import de.neuefische.CapStone.backend.model.AlarmEntity;
import de.neuefische.CapStone.backend.model.CoffeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AlarmRepository extends JpaRepository<AlarmEntity, Long> {

    Optional<AlarmEntity> findAlarmEntityByAlarmDevice_Uid(String uid);

    List<AlarmEntity> findAllByAlarmDevice_UserName(String username);
}
