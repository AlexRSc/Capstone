package de.neuefische.CapStone.backend.service;

import de.neuefische.CapStone.backend.api.LightDevice;
import de.neuefische.CapStone.backend.model.LightsDeviceEntity;
import de.neuefische.CapStone.backend.repo.LightsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LightsService {

    private final LightsRepository lightsRepository;


    @Autowired
    public LightsService(LightsRepository lightsRepository) {
        this.lightsRepository = lightsRepository;
    }

    public LightsDeviceEntity create(LightsDeviceEntity lightsDeviceEntity) {

        return lightsRepository.save(lightsDeviceEntity);
    }

    public LightsDeviceEntity find(LightDevice lightDevice) {
        Optional<LightsDeviceEntity> lightsDeviceEntity = lightsRepository.findByDevice_Uid(lightDevice.getUid());
        if(lightsDeviceEntity.isPresent()){
            return lightsDeviceEntity.get();
        }
        throw new IllegalArgumentException("LightsDevice not found");
    }

    public List<LightsDeviceEntity> getMyLightDevices(String userName) {

        return lightsRepository.findAllByDevice_UserName(userName);

    }
}
