package de.neuefische.CapStone.backend.controller;


import de.neuefische.CapStone.backend.api.CoffeeDevice;
import de.neuefische.CapStone.backend.api.User;
import de.neuefische.CapStone.backend.model.CoffeeEntity;
import de.neuefische.CapStone.backend.model.CoffeeStates;
import de.neuefische.CapStone.backend.model.Device;
import de.neuefische.CapStone.backend.model.UserEntity;
import de.neuefische.CapStone.backend.service.CoffeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.http.ResponseEntity.ok;

import static org.springframework.util.StringUtils.hasText;

@RestController
@RequestMapping("/coffee")
public class CoffeeController {

    private final CoffeeService coffeeService;

    @Autowired
    public CoffeeController(CoffeeService coffeeService) {
        this.coffeeService = coffeeService;
    }

    @PostMapping("/add")
    public ResponseEntity<CoffeeDevice> addCoffeeDevice(@AuthenticationPrincipal UserEntity authUser, @RequestBody CoffeeDevice coffeeDevice) {
        if(!hasText(coffeeDevice.getDeviceName())){
            throw new IllegalArgumentException("DeviceName can´t be blank");
        }
        if(!hasText(coffeeDevice.getUid())){
            throw new IllegalArgumentException("UID can´t be blank");
        }
        if(!hasText(coffeeDevice.getItemName())){
            throw new IllegalArgumentException("ItemName can't be blank!");
        }
        CoffeeEntity coffeeEntity = map(coffeeDevice, authUser);
        CoffeeEntity newCoffeeEntity = coffeeService.create(coffeeEntity);
        CoffeeDevice createdCoffeeDevice = map(newCoffeeEntity);
        return ok(createdCoffeeDevice);
    }

    private CoffeeDevice map(CoffeeEntity newCoffeeEntity) {
        return CoffeeDevice.builder()
                .date(newCoffeeEntity.getCoffeeStates().getDate())
                .itemName(newCoffeeEntity.getDevice().getItemName())
                .deviceName(newCoffeeEntity.getDevice().getDeviceName())
                .uid(newCoffeeEntity.getDevice().getUid())
                .onOff(newCoffeeEntity.getCoffeeStates().isOnOff()).build();
    }


    private CoffeeEntity map(CoffeeDevice coffeeDevice, UserEntity user) {
        Device device = Device.builder()
                .uid(coffeeDevice.getUid())
                .deviceName(coffeeDevice.getDeviceName())
                .itemName(coffeeDevice.getItemName())
                .userName(user.getUserName()).build();
        CoffeeStates coffeeStates = CoffeeStates.builder()
                .onOff(false)
                .date(coffeeDevice.getDate()).build();
        return CoffeeEntity.builder()
                .device(device)
                .coffeeStates(coffeeStates).build();
    }
}
