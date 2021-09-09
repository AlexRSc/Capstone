package de.neuefische.CapStone.backend.service;

import de.neuefische.CapStone.backend.model.HubEntity;
import de.neuefische.CapStone.backend.repo.HubRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HubService {

    private final HubRepository hubRepository;

    @Autowired
    public HubService(HubRepository hubRepository) {
        this.hubRepository = hubRepository;
    }

    public HubEntity createHub(HubEntity hubEntity, String userName) {
        if(hubRepository.existsByHubEmail(hubEntity.getHubEmail())){
            throw new IllegalArgumentException("HubEmail already exists!");
        }
        if(hubRepository.existsByUserName(userName)) {
            throw new IllegalArgumentException("You already have a Hub!");
        }
        hubEntity.setHubState("active");
        return hubRepository.save(hubEntity);

    }
}
