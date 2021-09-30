package de.neuefische.CapStone.backend.controller;
import de.neuefische.CapStone.backend.api.OnOffDevice;
import de.neuefische.CapStone.backend.model.*;
import de.neuefische.CapStone.backend.rest.openHab.OpenHabOnOffDto;
import de.neuefische.CapStone.backend.service.OnOffService;
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
@RequestMapping("/onoff")
public class OnOffController {

    private final OnOffService onOffService;
    private final OpenHabService openHabService;

    @Autowired
    public OnOffController(OnOffService onOffService, OpenHabService openHabService) {
        this.onOffService = onOffService;
        this.openHabService = openHabService;
    }

    @GetMapping("/myonoffdevices")
    public ResponseEntity<List<OnOffDevice>> getMyOnOffDevices (@AuthenticationPrincipal UserEntity authUser){
        List<OnOffDeviceEntity> onOffDeviceEntityList = onOffService.getMyOnOffDevices(authUser.getUserName());
        if(onOffDeviceEntityList.isEmpty()){
            return ok(null);
        }
        List<OnOffDevice> onOffDeviceList = map(onOffDeviceEntityList);
        return ok(onOffDeviceList);
    }


    @PostMapping("/add")
    public ResponseEntity<OnOffDevice> addNewOnOffDevice(@AuthenticationPrincipal UserEntity authUser,
                                                         @RequestBody OnOffDevice onOffDevice) {
        //refactor this into method, maybe in Service
        if (!hasText(onOffDevice.getUid())) {
            throw new IllegalArgumentException("UID cant be blank!");
        }
        if (!hasText(onOffDevice.getItemName())) {
            throw new IllegalArgumentException("ItemName cant be blank!");
        }
        if (!hasText(onOffDevice.getDeviceName())) {
            throw new IllegalArgumentException("DeviceName cant be blank!");
        }
        OnOffDeviceEntity onOffDeviceEntity = map(onOffDevice, authUser);
        OnOffDeviceEntity newOnOffDeviceEntity = onOffService.createOnOffDevice(onOffDeviceEntity);
        OnOffDevice newOnOffDevice = map(newOnOffDeviceEntity);
        return ok(newOnOffDevice);

    }


    @PostMapping("/turnon")
    public ResponseEntity<OpenHabOnOffDto> turnLightsDeviceOn(@RequestBody OnOffDevice onOffDevice) {

        OnOffDeviceEntity onOffDeviceEntity = onOffService.find(onOffDevice);
        return openHabService.turnOnOnOffDevice(onOffDeviceEntity);

    }

    @PostMapping("/turnoff")
    public ResponseEntity<OpenHabOnOffDto> turnLightsDeviceOff(@RequestBody OnOffDevice onOffDevice) {

        OnOffDeviceEntity onOffDeviceEntity = onOffService.find(onOffDevice);
        return openHabService.turnOFFOnOffDevice(onOffDeviceEntity);
    }

    @DeleteMapping("/delete/{uid}")
    public ResponseEntity<OnOffDevice> deleteOnOffDevice(@PathVariable String uid) {
        OnOffDeviceEntity onOffDeviceEntity = onOffService.findLightByUid(uid);
        OnOffDeviceEntity deleteOnOffDeviceEntity = onOffService.deleteLightsDevice(onOffDeviceEntity);
        return ok(map(deleteOnOffDeviceEntity));
    }


    private List<OnOffDevice> map(List<OnOffDeviceEntity> onOffDeviceEntityList) {
        List<OnOffDevice> onOffDeviceList = new LinkedList<>();
        for(OnOffDeviceEntity onOffDeviceEntity:onOffDeviceEntityList){
            OnOffDevice onOffDevice = OnOffDevice.builder()
                    .deviceName(onOffDeviceEntity.getDevice().getDeviceName())
                    .uid(onOffDeviceEntity.getDevice().getUid())
                    .itemName(onOffDeviceEntity.getDevice().getItemName())
                    .onOff(onOffDeviceEntity.getOnOffDeviceStates().isOnOff()).build();
            onOffDeviceList.add(onOffDevice);
        }
        return onOffDeviceList;
    }


    private OnOffDevice map(OnOffDeviceEntity newOnOffDeviceEntity) {
        return   OnOffDevice.builder()
                .deviceName(newOnOffDeviceEntity.getDevice().getDeviceName())
                .itemName(newOnOffDeviceEntity.getDevice().getItemName())
                .uid(newOnOffDeviceEntity.getDevice().getUid())
                .onOff(newOnOffDeviceEntity.getOnOffDeviceStates().isOnOff()).build();
    }


    private OnOffDeviceEntity map(OnOffDevice onOffDevice, UserEntity authUser) {
        Device device = Device.builder()
                .deviceName(onOffDevice.getDeviceName())
                .itemName(onOffDevice.getItemName())
                .userName(authUser.getUserName())
                .uid(onOffDevice.getUid()).build();
        OnOffDeviceStates onOffDeviceStates = OnOffDeviceStates.builder()
                .onOff(onOffDevice.isOnOff()).build();
        return OnOffDeviceEntity.builder()
                .device(device)
                .onOffDeviceStates(onOffDeviceStates).build();
    }
}
