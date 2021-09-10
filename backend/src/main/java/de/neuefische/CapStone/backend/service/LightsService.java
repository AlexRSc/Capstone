package de.neuefische.CapStone.backend.service;

import de.neuefische.CapStone.backend.api.LightDevice;
import de.neuefische.CapStone.backend.model.HubEntity;
import de.neuefische.CapStone.backend.model.LightsDeviceEntity;
import de.neuefische.CapStone.backend.repo.HubRepository;
import de.neuefische.CapStone.backend.repo.LightsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LightsService {

    private final LightsRepository lightsRepository;
    private final HubRepository hubRepository;


    @Autowired
    public LightsService(LightsRepository lightsRepository, HubRepository hubRepository) {
        this.lightsRepository = lightsRepository;
        this.hubRepository = hubRepository;
    }

    public LightsDeviceEntity create(LightsDeviceEntity lightsDeviceEntity) {
        Optional<HubEntity> hubEntityOptional= hubRepository.findHubEntityByUserName(lightsDeviceEntity.getDevice().getUserName());
        if (hubEntityOptional.isEmpty()){
            throw new IllegalArgumentException("You don´t have a Hub");
        }
        lightsDeviceEntity.getDevice().setHubId(hubEntityOptional.get().getId());
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
