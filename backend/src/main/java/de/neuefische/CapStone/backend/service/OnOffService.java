package de.neuefische.CapStone.backend.service;

import de.neuefische.CapStone.backend.api.OnOffDevice;
import de.neuefische.CapStone.backend.model.Device;
import de.neuefische.CapStone.backend.model.HubEntity;
import de.neuefische.CapStone.backend.model.LightsDeviceEntity;
import de.neuefische.CapStone.backend.model.OnOffDeviceEntity;
import de.neuefische.CapStone.backend.repo.OnOffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class OnOffService {

    private final OnOffRepository onOffRepository;
    private final HubService hubService;

    @Autowired
    public OnOffService(OnOffRepository onOffRepository, HubService hubService) {
        this.onOffRepository = onOffRepository;
        this.hubService = hubService;
    }

    public OnOffDeviceEntity createOnOffDevice(OnOffDeviceEntity onOffDeviceEntity) {
        if (onOffRepository.existsOnOffDeviceEntitiesByDevice_Uid(onOffDeviceEntity.getDevice().getUid())) {
            throw new IllegalArgumentException("This Device already exists in our Database!");
        }
        HubEntity hubEntity = hubService.findHubByUserName(onOffDeviceEntity.getDevice().getUserName());
        onOffDeviceEntity.setHubEntity(hubEntity);
        return onOffRepository.save(onOffDeviceEntity);
    }

    public OnOffDeviceEntity find(OnOffDevice onOffDevice) {
        Optional<OnOffDeviceEntity> onOffDeviceEntityOptional = onOffRepository.findByDevice_Uid(onOffDevice.getUid());
        if(onOffDeviceEntityOptional.isPresent()){
            return onOffDeviceEntityOptional.get();
        }
        throw new IllegalArgumentException("OnOffDevice not found!");
    }

    public void findAndTurnOnOff(Device device, boolean onOff) {
        Optional<OnOffDeviceEntity> onOffDeviceEntityOptional = onOffRepository.findByDevice_Uid(device.getUid());
        if(onOffDeviceEntityOptional.isPresent()){
            OnOffDeviceEntity onOffDeviceEntity = onOffDeviceEntityOptional.get();
            onOffDeviceEntity.getOnOffDeviceStates().setOnOff(onOff);
            onOffRepository.save(onOffDeviceEntity);
        }
    }

    public List<OnOffDeviceEntity> getMyOnOffDevices(String userName) {
        return onOffRepository.findAllByDevice_UserName(userName);
    }
}
