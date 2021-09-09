package de.neuefische.CapStone.backend.service;

import de.neuefische.CapStone.backend.model.LightsDeviceEntity;
import de.neuefische.CapStone.backend.repo.LightsRepository;
import org.springframework.stereotype.Service;

@Service
public class LightsService {

    private final LightsRepository lightsRepository;

    public LightsService(LightsRepository lightsRepository) {
        this.lightsRepository = lightsRepository;
    }

    public LightsDeviceEntity create(LightsDeviceEntity lightsDeviceEntity) {

        return lightsRepository.save(lightsDeviceEntity);
    }
}
