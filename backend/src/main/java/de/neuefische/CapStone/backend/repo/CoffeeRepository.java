package de.neuefische.CapStone.backend.repo;

import de.neuefische.CapStone.backend.model.CoffeeEntity;
import de.neuefische.CapStone.backend.model.LightsDeviceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CoffeeRepository extends JpaRepository<CoffeeEntity, Long> {

    Optional<CoffeeEntity> findByDevice_Uid(String uid);

    List<CoffeeEntity> findAllByDevice_UserName(String userName);

}
