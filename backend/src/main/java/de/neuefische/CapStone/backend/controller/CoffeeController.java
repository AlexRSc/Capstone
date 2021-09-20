package de.neuefische.CapStone.backend.controller;


import de.neuefische.CapStone.backend.api.CoffeeDevice;
import de.neuefische.CapStone.backend.model.CoffeeEntity;
import de.neuefische.CapStone.backend.model.CoffeeStates;
import de.neuefische.CapStone.backend.model.Device;
import de.neuefische.CapStone.backend.model.UserEntity;
import de.neuefische.CapStone.backend.service.CoffeeService;
import de.neuefische.CapStone.backend.service.OpenHabService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.ResponseEntity.ok;

import static org.springframework.util.StringUtils.hasText;

@RestController
@RequestMapping("/coffee")
public class CoffeeController {
    private final CoffeeService coffeeService;
    private final OpenHabService openHabService;

    @Autowired
    public CoffeeController(CoffeeService coffeeService, OpenHabService openHabService) {
        this.coffeeService = coffeeService;
        this.openHabService = openHabService;
    }

    @GetMapping("/getMyCoffee")
    public ResponseEntity<List<CoffeeDevice>> getMyCoffeeDevices(@AuthenticationPrincipal UserEntity authUser) {
        List<CoffeeEntity> coffeeEntityList = coffeeService.getCoffeeList(authUser.getUserName());
        List<CoffeeDevice> myCoffeeDevices = map(coffeeEntityList);
        return ok(myCoffeeDevices);
    }


    @PostMapping("/add")
    public ResponseEntity<CoffeeDevice> addCoffeeDevice(@AuthenticationPrincipal UserEntity authUser, @RequestBody CoffeeDevice coffeeDevice) {
        checkInput(coffeeDevice);
        CoffeeEntity coffeeEntity = map(coffeeDevice, authUser);
        CoffeeEntity newCoffeeEntity = coffeeService.create(coffeeEntity);
        CoffeeDevice createdCoffeeDevice = map(newCoffeeEntity);
        return ok(createdCoffeeDevice);
    }

    @PutMapping("/newschedule")
    public ResponseEntity<CoffeeDevice> addNewCoffeeSchedule(@AuthenticationPrincipal UserEntity authUser, @RequestBody CoffeeDevice coffeeDevice) {
        checkInput(coffeeDevice);
        CoffeeEntity coffeeEntity = map(coffeeDevice, authUser);
        CoffeeEntity newCoffeeEntity = coffeeService.addNewSchedule(coffeeEntity);
        return ok(map(newCoffeeEntity));
    }

    @PostMapping("/turnOn")
    public ResponseEntity<CoffeeDevice> turnOnCoffeeMachine(@AuthenticationPrincipal UserEntity authUser, @RequestBody CoffeeDevice coffeeDevice) {
        checkInput(coffeeDevice);
        CoffeeEntity coffeeEntity = map(coffeeDevice, authUser);
        CoffeeEntity fullCoffeeEntity = coffeeService.changeOnOffToOn(coffeeEntity);
        openHabService.turnCoffeeMachineOn(fullCoffeeEntity);
        return ok(coffeeDevice);
    }

    @PostMapping("/turnOff")
    public ResponseEntity<CoffeeDevice> turnOFFCoffeeMachine(@AuthenticationPrincipal UserEntity authUser, @RequestBody CoffeeDevice coffeeDevice) {
        checkInput(coffeeDevice);
        CoffeeEntity coffeeEntity = map(coffeeDevice, authUser);
        CoffeeEntity fullCoffeeEntity = coffeeService.changeOnOffToOFF(coffeeEntity);
        openHabService.turnCoffeeMachineOFF(fullCoffeeEntity);
        return ok(coffeeDevice);
    }

    @PutMapping("/deactivate")
    public ResponseEntity<CoffeeDevice> deactivateCoffeeEvent(@AuthenticationPrincipal UserEntity authUser, @RequestBody CoffeeDevice coffeeDevice) {
        checkInput(coffeeDevice);
        CoffeeEntity coffeeEntity = coffeeService.deactivateCoffeeEvent(map(coffeeDevice, authUser));
        return ok(map(coffeeEntity));
    }

    @PutMapping("/activate")
    public ResponseEntity<CoffeeDevice> activateCoffeeEvent(@AuthenticationPrincipal UserEntity authUser, @RequestBody CoffeeDevice coffeeDevice) {
        checkInput(coffeeDevice);
        CoffeeEntity coffeeEntity = coffeeService.activateCoffeeEvent(map(coffeeDevice,authUser));
        return ok(map(coffeeEntity));
    }

    @DeleteMapping("/delete/{uid}")
    public ResponseEntity<CoffeeDevice> deleteCoffeeDevice(@PathVariable String uid) {
        CoffeeEntity coffeeEntity = coffeeService.findCoffeeByUid(uid);
        CoffeeEntity deletedCoffeeEntity = coffeeService.deleteCoffeeDevice(coffeeEntity);
        return ok(map(deletedCoffeeEntity));
    }

    private void checkInput(CoffeeDevice coffeeDevice) {
        if (!hasText(coffeeDevice.getDeviceName())) {
            throw new IllegalArgumentException("DeviceName can´t be blank");
        }
        if (!hasText(coffeeDevice.getUid())) {
            throw new IllegalArgumentException("UID can´t be blank");
        }
        if (!hasText(coffeeDevice.getItemName())) {
            throw new IllegalArgumentException("ItemName can't be blank!");
        }
        if (coffeeDevice.getDate() == null) {
            throw new IllegalArgumentException("Date can't be blank!");
        }
    }

    private List<CoffeeDevice> map(List<CoffeeEntity> coffeeEntityList) {
        List<CoffeeDevice> coffeeDeviceList = new LinkedList<>();
        for (CoffeeEntity coffeeEntity : coffeeEntityList) {
            CoffeeDevice coffeeDevice = map(coffeeEntity);
            coffeeDeviceList.add(coffeeDevice);
        }
        return coffeeDeviceList;
    }

    private CoffeeDevice map(CoffeeEntity newCoffeeEntity) {
        return CoffeeDevice.builder()
                .date(Date.from(newCoffeeEntity.getCoffeeStates().getDate()))
                .itemName(newCoffeeEntity.getDevice().getItemName())
                .deviceName(newCoffeeEntity.getDevice().getDeviceName())
                .uid(newCoffeeEntity.getDevice().getUid())
                .onOff(newCoffeeEntity.getCoffeeStates().isOnOff())
                .dailyAction(newCoffeeEntity.getCoffeeStates().isDailyAction())
                .eventActive(newCoffeeEntity.getCoffeeStates().isEventActive()).build();
    }


    private CoffeeEntity map(CoffeeDevice coffeeDevice, UserEntity user) {
        Device device = Device.builder()
                .uid(coffeeDevice.getUid())
                .deviceName(coffeeDevice.getDeviceName())
                .itemName(coffeeDevice.getItemName())
                .userName(user.getUserName()).build();
        CoffeeStates coffeeStates = CoffeeStates.builder()
                .onOff(false)
                .date(coffeeDevice.getDate().toInstant())
                .dailyAction(coffeeDevice.isDailyAction()).build();
        return CoffeeEntity.builder()
                .device(device)
                .coffeeStates(coffeeStates).build();
    }
}
