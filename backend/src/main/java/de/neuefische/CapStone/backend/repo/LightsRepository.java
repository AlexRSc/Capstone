package de.neuefische.CapStone.backend.repo;

import de.neuefische.CapStone.backend.model.HubEntity;
import de.neuefische.CapStone.backend.model.LightsDeviceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LightsRepository extends JpaRepository<LightsDeviceEntity, Long> {

    Optional<LightsDeviceEntity> findByDevice_Uid(String uid);
}
