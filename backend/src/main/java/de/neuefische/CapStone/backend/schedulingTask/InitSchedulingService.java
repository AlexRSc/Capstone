package de.neuefische.CapStone.backend.schedulingTask;

import de.neuefische.CapStone.backend.model.AlarmEventEntity;
import de.neuefische.CapStone.backend.model.CoffeeEntity;
import de.neuefische.CapStone.backend.service.AlarmEventService;
import de.neuefische.CapStone.backend.service.CoffeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class InitSchedulingService {

    private final CoffeeService coffeeService;
    private final AlarmEventService alarmEventService;
    private static final Logger log = LoggerFactory.getLogger(CoffeeEntity.class);

    @Autowired
    public InitSchedulingService(CoffeeService coffeeService, AlarmEventService alarmEventService) {
        this.coffeeService = coffeeService;
        this.alarmEventService = alarmEventService;
    }

    @Async
    @Scheduled(initialDelay = 1000, fixedDelay = Long.MAX_VALUE)
    public void initialCoffeeEvents() {
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

    @Async
    @Scheduled(initialDelay = 1500, fixedDelay = Long.MAX_VALUE)
    public void initialAlarmEvents() {
        List<AlarmEventEntity> alarmEventEntityList = alarmEventService.getAllEvents();
        if(alarmEventEntityList.isEmpty()) {
            log.info("No AlarmEvents in DB!");
        } else {
            for(AlarmEventEntity alarmEventEntity : alarmEventEntityList) {
                if(alarmEventEntity.getDate().isAfter(Instant.now())&&alarmEventEntity.isEvent()){
                    log.info("turning on "+alarmEventEntity.getId());
                    alarmEventService.manageEventStart(alarmEventEntity);
                    alarmEventService.manageEventStop(alarmEventEntity);
                } else {
                    if(alarmEventEntity.isDaily()&&alarmEventEntity.isEvent()) {
                        log.info("turning on daily "+alarmEventEntity.getId());
                        alarmEventService.manageEventStart(alarmEventEntity);
                        alarmEventService.manageEventStop(alarmEventEntity);
                    } else {
                        log.info("Event inactive: " +alarmEventEntity.getId());
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
