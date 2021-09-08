package de.neuefische.CapStone.backend.service;

import de.neuefische.CapStone.backend.model.HubEntity;
import de.neuefische.CapStone.backend.repo.HubRepository;
import org.springframework.stereotype.Service;

@Service
public class HubService {

    private final HubRepository hubRepository;

    public HubService(HubRepository hubRepository) {
        this.hubRepository = hubRepository;
    }

    public HubEntity createHub(HubEntity hubEntity) {
        if(hubRepository.existsByHubEmail(hubEntity.getHubEmail())){
            throw new IllegalArgumentException("HubEmail already exists!");
        }

        return hubRepository.save(hubEntity);

    }
}
