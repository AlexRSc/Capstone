package de.neuefische.CapStone.backend.repo;


import de.neuefische.CapStone.backend.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Boolean existsByUserName(String userName);

    Boolean existsByEmail(String email);
}
