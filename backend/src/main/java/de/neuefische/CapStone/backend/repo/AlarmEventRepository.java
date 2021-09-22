package de.neuefische.CapStone.backend.repo;

import de.neuefische.CapStone.backend.model.AlarmEntity;
import de.neuefische.CapStone.backend.model.AlarmEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmEventRepository extends JpaRepository<AlarmEventEntity, Long> {
}
