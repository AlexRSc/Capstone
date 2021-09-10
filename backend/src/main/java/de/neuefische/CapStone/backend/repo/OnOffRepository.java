package de.neuefische.CapStone.backend.repo;

import de.neuefische.CapStone.backend.model.OnOffDeviceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OnOffRepository extends JpaRepository<OnOffDeviceEntity, Long> {

    boolean existsOnOffDeviceEntitiesByDevice_Uid(String onOffUid);

    Optional<OnOffDeviceEntity> findByDevice_Uid(String uid);
}
