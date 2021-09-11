package de.neuefische.CapStone.backend.repo;

import de.neuefische.CapStone.backend.api.Hub;
import de.neuefische.CapStone.backend.model.HubEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HubRepository extends JpaRepository<HubEntity, Long> {

    Boolean existsByHubEmail(String hubEmail);

    Optional<HubEntity> findHubEntitiesById(long hubId);

    Boolean existsByUserName(String username);

    Optional<HubEntity> findHubEntityByUserName(String userName);

}
