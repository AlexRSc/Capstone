package de.neuefische.CapStone.backend.controller;

import de.neuefische.CapStone.backend.api.Hub;
import de.neuefische.CapStone.backend.api.LightDevice;
import de.neuefische.CapStone.backend.model.*;
import de.neuefische.CapStone.backend.service.LightsService;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.event.internal.DefaultEvictEventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static org.springframework.util.StringUtils.hasText;

@CrossOrigin
@RestController
@Getter
@Setter
@RequestMapping("/lights")
public class LightsController {

    private final LightsService lightsService;

    public LightsController(LightsService lightsService) {
        this.lightsService = lightsService;
    }

    @PostMapping("/add")
    public ResponseEntity<LightDevice> addLightsDevice(@AuthenticationPrincipal UserEntity authUser,
                                                       @RequestBody LightDevice lightDevice) {

        if(!hasText(lightDevice.getUid())){
            throw new IllegalArgumentException("UID cant be blank!");
        }
        if(!hasText(lightDevice.getItemName())){
            throw new IllegalArgumentException("ItemName cant be blank!");
        }
        if(!hasText(lightDevice.getDeviceName())){
            throw new IllegalArgumentException("DeviceName cant be blank!");
        }
        LightsDeviceEntity lightsDeviceEntity = map(lightDevice, authUser);
        LightsDeviceEntity createdLightsDeviceEntity = lightsService.create(lightsDeviceEntity);
        LightDevice newLightDevice = map(createdLightsDeviceEntity);
        return ResponseEntity.ok(newLightDevice);

    }

    private LightDevice map(LightsDeviceEntity createdLightsDeviceEntity) {
        return LightDevice.builder()
                .deviceName(createdLightsDeviceEntity.getDevice().getDeviceName())
                .uid(createdLightsDeviceEntity.getDevice().getUid())
                .itemName(createdLightsDeviceEntity.getDevice().getItemName())
                .brightness(createdLightsDeviceEntity.getDeviceStates().isBrightness())
                .onOff(createdLightsDeviceEntity.getDeviceStates().isOnOff()).build();
    }

    private LightsDeviceEntity map(LightDevice lightDevice, UserEntity authUser) {
        Device device = Device.builder()
                .userName(authUser.getUserName())
                .uid(lightDevice.getUid())
                .itemName(lightDevice.getItemName())
                .deviceName(lightDevice.getDeviceName()).build();
        DeviceStates deviceStates = DeviceStates.builder()
                .brightness(true)
                .onOff(true).build();
        return LightsDeviceEntity.builder()
                .device(device)
                .deviceStates(deviceStates).build();
    }
}
