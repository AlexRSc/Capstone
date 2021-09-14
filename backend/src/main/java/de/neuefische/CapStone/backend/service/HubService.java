package de.neuefische.CapStone.backend.service;

import de.neuefische.CapStone.backend.model.HubEntity;
import de.neuefische.CapStone.backend.model.UserEntity;
import de.neuefische.CapStone.backend.repo.HubRepository;
import de.neuefische.CapStone.backend.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class HubService {

    private final HubRepository hubRepository;
    private final UserRepository userRepository;

    @Autowired
    public HubService(HubRepository hubRepository, UserRepository userRepository) {
        this.hubRepository = hubRepository;
        this.userRepository = userRepository;
    }

    public HubEntity createHub(HubEntity hubEntity, String userName) {
        if(hubRepository.existsByHubEmail(hubEntity.getHubEmail())){
            throw new IllegalArgumentException("HubEmail already exists!");
        }
        if(hubRepository.existsByUserName(userName)) {
            throw new IllegalArgumentException("You already have a Hub!");
        }
        Optional<UserEntity> userEntityOptional = userRepository.findByUserName(userName);
        if(userEntityOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found!");
        }
        UserEntity userEntity = userEntityOptional.get();
        hubEntity.setUserEntity(userEntity);
        hubEntity.setHubState("active");
        return hubRepository.save(hubEntity);
    }

    public HubEntity findHubByUserName(String userName) {
        Optional<HubEntity> hubEntityOptional= hubRepository.findHubEntityByUserName(userName);
        if(hubEntityOptional.isEmpty()){
            throw new IllegalArgumentException("You haven´t setup a Hub yet!");
        }
        return hubEntityOptional.get();
    }
}
