package de.neuefische.CapStone.backend.schedulingTask;

import de.neuefische.CapStone.backend.model.CoffeeEntity;
import de.neuefische.CapStone.backend.service.CoffeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.config.ScheduledTask;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class InitSchedulingService {

    private final CoffeeService coffeeService;
    private static final Logger log = LoggerFactory.getLogger(CoffeeEntity.class);

    @Autowired
    public InitSchedulingService(CoffeeService coffeeService) {
        this.coffeeService = coffeeService;
    }

    @Async
    @Scheduled(initialDelay = 1000, fixedDelay = Long.MAX_VALUE)
    public void executeInitial() {
        List<CoffeeEntity> coffeeEntityList = coffeeService.getAllCoffees();
        if (coffeeEntityList.isEmpty()) {
            log.info("Coffee DB still empty!");
        } else {
            for (CoffeeEntity coffeeEntity : coffeeEntityList) {
                if (coffeeEntity.getCoffeeStates().getDate().isAfter(Instant.now())&&coffeeEntity.getCoffeeStates().isEventActive()) {
                    log.info("turning on :" + coffeeEntity.getDevice().getDeviceName());
                    coffeeService.manageCoffeeTurnOn(coffeeEntity);
                    coffeeService.manageCoffeeTurnOff(coffeeEntity);
                } else {
                    if (coffeeEntity.getCoffeeStates().isDailyAction()&&coffeeEntity.getCoffeeStates().isEventActive()) {
                        log.info("turning on :" + coffeeEntity.getDevice().getDeviceName());
                        coffeeService.manageCoffeeTurnOn(coffeeEntity);
                        coffeeService.manageCoffeeTurnOff(coffeeEntity);
                    } else {
                        log.info("Device event date already happened for :" + coffeeEntity.getDevice().getDeviceName());
                    }
                }
            }
        }
    }

    //Scheduled works in ms, which means 300000 is 5 min
    @Async
    @Scheduled(initialDelay = 60000, fixedDelay = 300000)
    public void updatingStates() {
        List<CoffeeEntity> coffeeEntityList = coffeeService.getAllCoffees();
        if (coffeeEntityList.isEmpty()) {
            log.info("Coffee DB still empty!");
        } else {
            for (CoffeeEntity coffeeEntity : coffeeEntityList) {
                if (Instant.now().isAfter(coffeeEntity.getCoffeeStates().getDate())&&(!coffeeEntity.getCoffeeStates().isDailyAction())) {
                    if(coffeeEntity.getCoffeeStates().isEventActive()) {
                        log.info("setting to inactive :" + coffeeEntity.getDevice().getDeviceName());
                        coffeeService.setCoffeeDeviceToInactive(coffeeEntity);
                    }
                    else {
                        log.info("This device was already inactive: " +coffeeEntity.getDevice().getDeviceName());
                    }
                } else {
                    log.info("This device is programed to be daily: " + coffeeEntity.getDevice().getDeviceName());
                }
            }
        }
    }
}
