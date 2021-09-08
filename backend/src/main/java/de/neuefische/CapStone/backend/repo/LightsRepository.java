package de.neuefische.CapStone.backend.repo;

import de.neuefische.CapStone.backend.model.HubEntity;
import de.neuefische.CapStone.backend.model.LightsDeviceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LightsRepository extends JpaRepository<LightsDeviceEntity, Long> {
}
