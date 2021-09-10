package de.neuefische.CapStone.backend.service;

import de.neuefische.CapStone.backend.api.OnOffDevice;
import de.neuefische.CapStone.backend.model.OnOffDeviceEntity;
import de.neuefische.CapStone.backend.repo.OnOffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class OnOffService {

    private final OnOffRepository onOffRepository;

    @Autowired
    public OnOffService(OnOffRepository onOffRepository) {
        this.onOffRepository = onOffRepository;
    }

    public OnOffDeviceEntity createOnOffDevice(OnOffDeviceEntity onOffDeviceEntity) {
        if (onOffRepository.existsOnOffDeviceEntitiesByDevice_Uid(onOffDeviceEntity.getDevice().getUid())) {
            throw new IllegalArgumentException("This Device already exists in our Database!");
        }

        return onOffRepository.save(onOffDeviceEntity);
    }

    public OnOffDeviceEntity find(OnOffDevice onOffDevice) {
        Optional<OnOffDeviceEntity> onOffDeviceEntityOptional = onOffRepository.findByDevice_Uid(onOffDevice.getUid());
        if(onOffDeviceEntityOptional.isPresent()){
            return onOffDeviceEntityOptional.get();
        }
        throw new IllegalArgumentException("OnOffDevice not found!");
    }

    public List<OnOffDeviceEntity> getMyOnOffDevices(String userName) {
        return onOffRepository.findAllByDevice_UserName(userName);
    }
}
