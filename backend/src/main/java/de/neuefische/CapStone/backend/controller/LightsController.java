package de.neuefische.CapStone.backend.controller;

import de.neuefische.CapStone.backend.api.LightDevice;
import de.neuefische.CapStone.backend.api.LightsDeviceAPIDto;
import de.neuefische.CapStone.backend.model.*;
import de.neuefische.CapStone.backend.rest.openHab.OpenHabLightsBrightnessDto;
import de.neuefische.CapStone.backend.rest.openHab.OpenHabOnOffDto;
import de.neuefische.CapStone.backend.service.LightsService;
import de.neuefische.CapStone.backend.service.OpenHabService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.LinkedList;
import java.util.List;

import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.util.StringUtils.hasText;

@CrossOrigin
@RestController
@Getter
@Setter
@RequestMapping("/lights")
public class LightsController {

    private final LightsService lightsService;
    private final OpenHabService openHabService;

    @Autowired
    public LightsController(LightsService lightsService, OpenHabService openHabService) {
        this.lightsService = lightsService;
        this.openHabService = openHabService;
    }

    @GetMapping("/mylightdevices")
    public ResponseEntity<List<LightsDeviceAPIDto>> getMyLightDevices (@AuthenticationPrincipal UserEntity authUser) {
        List<LightsDeviceEntity> lightsDeviceEntityList = lightsService.getMyLightDevices(authUser.getUserName());
        if(lightsDeviceEntityList.isEmpty()){
            return ok(null);
        }
        List<LightsDeviceAPIDto> lightsDeviceAPIDtoList = map(lightsDeviceEntityList);
        return ok(lightsDeviceAPIDtoList);
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
        return ok(newLightDevice);

    }

    @PostMapping("/turnon")
    public ResponseEntity<OpenHabOnOffDto> turnLightsDeviceOn(@AuthenticationPrincipal UserEntity authUser,
                                                              @RequestBody LightDevice lightDevice) {

        LightsDeviceEntity lightsDeviceEntity= lightsService.find(lightDevice);
        return openHabService.turnOn(lightsDeviceEntity.getDevice());

    }

    @PostMapping("/turnoff")
    public ResponseEntity<OpenHabOnOffDto> turnLightsDeviceOff(@AuthenticationPrincipal UserEntity authUser,
                                                              @RequestBody LightDevice lightDevice) {
        LightsDeviceEntity lightsDeviceEntity = lightsService.find(lightDevice);
        return openHabService.turnOff(lightsDeviceEntity.getDevice());

    }
    @PostMapping("/brightness")
    public ResponseEntity<OpenHabLightsBrightnessDto> changeLightsDeviceBrightness(@AuthenticationPrincipal UserEntity authUser,
                                                                                   @RequestBody LightDevice lightDevice) {
        LightsDeviceEntity lightsDeviceEntity = lightsService.find(lightDevice);
        String brightness = lightDevice.getBrightness();
        return openHabService.changeBrightness(lightsDeviceEntity.getDevice(), brightness);
    }

    private List<LightsDeviceAPIDto> map(List<LightsDeviceEntity> lightsDeviceEntityList) {
        List<LightsDeviceAPIDto> lightsDeviceAPIDtoList = new LinkedList<>();
        for(LightsDeviceEntity lightsDeviceEntity : lightsDeviceEntityList){
            LightsDeviceAPIDto lightsDeviceAPIDto = LightsDeviceAPIDto.builder()
                    .deviceName(lightsDeviceEntity.getDevice().getDeviceName())
                    .itemName(lightsDeviceEntity.getDevice().getItemName())
                    .uid(lightsDeviceEntity.getDevice().getUid())
                    .brightness(lightsDeviceEntity.getLightsDeviceStates().isBrightness())
                    .onOff(lightsDeviceEntity.getLightsDeviceStates().isOnOff()).build();
            lightsDeviceAPIDtoList.add(lightsDeviceAPIDto);
        }
        return lightsDeviceAPIDtoList;
    }

    private LightDevice map(LightsDeviceEntity createdLightsDeviceEntity) {
        return LightDevice.builder()
                .deviceName(createdLightsDeviceEntity.getDevice().getDeviceName())
                .uid(createdLightsDeviceEntity.getDevice().getUid())
                .itemName(createdLightsDeviceEntity.getDevice().getItemName())
                .onOff(createdLightsDeviceEntity.getLightsDeviceStates().isOnOff()).build();
    }

    private LightsDeviceEntity map(LightDevice lightDevice, UserEntity authUser) {
        Device device = Device.builder()
                .userName(authUser.getUserName())
                .uid(lightDevice.getUid())
                .itemName(lightDevice.getItemName())
                .deviceName(lightDevice.getDeviceName()).build();
        LightsDeviceStates lightsDeviceStates = LightsDeviceStates.builder()
                .brightness(true)
                .onOff(true).build();
        return LightsDeviceEntity.builder()
                .device(device)
                .lightsDeviceStates(lightsDeviceStates).build();
    }
}
