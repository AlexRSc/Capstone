package de.neuefische.CapStone.backend.service;

import de.neuefische.CapStone.backend.model.CoffeeEntity;
import de.neuefische.CapStone.backend.model.HubEntity;
import de.neuefische.CapStone.backend.repo.CoffeeRepository;
import de.neuefische.CapStone.backend.repo.HubRepository;
import de.neuefische.CapStone.backend.schedulingTask.ScheduleService;
import de.neuefische.CapStone.backend.schedulingTask.TaskDefinition;
import de.neuefische.CapStone.backend.schedulingTask.TaskSchedulingService;
import de.neuefische.CapStone.backend.schedulingTask.TurnOffScheduleService;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class CoffeeService {

    private final CoffeeRepository coffeeRepository;
    private final HubRepository hubRepository;
    private final ScheduleService scheduleService;
    private final TaskSchedulingService taskSchedulingService;
    private final CronService cronService;
    private final TurnOffScheduleService turnOffScheduleService;

    public CoffeeService(CoffeeRepository coffeeRepository, HubRepository hubRepository, ScheduleService scheduleService, TaskSchedulingService taskSchedulingService, CronService cronService, TurnOffScheduleService turnOffScheduleService) {
        this.coffeeRepository = coffeeRepository;
        this.hubRepository = hubRepository;
        this.scheduleService = scheduleService;
        this.taskSchedulingService = taskSchedulingService;
        this.cronService = cronService;
        this.turnOffScheduleService = turnOffScheduleService;
    }

    public CoffeeEntity findCoffeeByUid(String uid) {
        Optional<CoffeeEntity> coffeeEntityOptional = coffeeRepository.findByDevice_Uid(uid);
        if(coffeeEntityOptional.isEmpty()) {
            throw new EntityNotFoundException("Device couldnt be found!");
        }
        return coffeeEntityOptional.get();
    }

    public CoffeeEntity create(CoffeeEntity coffeeEntity) {
        Optional<HubEntity> hubEntityOptional = hubRepository.findHubEntityByUserName(coffeeEntity.getDevice().getUserName());
        if (hubEntityOptional.isEmpty()) {
            throw new IllegalArgumentException("You don´t have a Hub");
        }
        Optional<CoffeeEntity> coffeeEntityOptional = coffeeRepository.findByDevice_Uid(coffeeEntity.getDevice().getUid());
        if (coffeeEntityOptional.isPresent()) {
            throw new IllegalArgumentException("You already have that Coffee Maker!");
        }
        HubEntity hubEntity = hubEntityOptional.get();
        coffeeEntity.setHubEntity(hubEntity);
        coffeeEntity.getCoffeeStates().setEventActive(true);
        coffeeRepository.saveAndFlush(coffeeEntity);
        Optional<CoffeeEntity> createdCoffeeEntityOptional = coffeeRepository.findByDevice_Uid(coffeeEntity.getDevice().getUid());
        if (createdCoffeeEntityOptional.isPresent()) {
            manageCoffeeTurnOn(coffeeEntity);
            manageCoffeeTurnOff(coffeeEntity);
        } else {
            throw new IllegalArgumentException("Oops, something went wrong!");
        }
        return coffeeEntity;
    }
    public CoffeeEntity addNewSchedule(CoffeeEntity coffeeEntity) {
        Optional<CoffeeEntity> coffeeEntityOptional = coffeeRepository.findByDevice_Uid(coffeeEntity.getDevice().getUid());
        if(coffeeEntityOptional.isEmpty()){
            throw new EntityNotFoundException("We couldnt find your Coffee Machine!");
        }
        coffeeEntityOptional.get().getCoffeeStates().setDailyAction(coffeeEntity.getCoffeeStates().isDailyAction());
        coffeeEntityOptional.get().getCoffeeStates().setDate(coffeeEntity.getCoffeeStates().getDate());
        coffeeEntityOptional.get().getCoffeeStates().setEventActive(true);
        manageCoffeeTurnOn(coffeeEntityOptional.get());
        manageCoffeeTurnOff(coffeeEntityOptional.get());
        return coffeeRepository.save(coffeeEntityOptional.get());

    }

    public CoffeeEntity deactivateCoffeeEvent(CoffeeEntity coffeeEntity) {
        Optional<CoffeeEntity> coffeeEntityOptional = coffeeRepository.findByDevice_Uid(coffeeEntity.getDevice().getUid());
        if(coffeeEntityOptional.isEmpty()){
            throw new EntityNotFoundException("Coffee Device wasn't found!");
        }
        CoffeeEntity deactivatedCoffeeEntity = coffeeEntityOptional.get();
        deactivatedCoffeeEntity.getCoffeeStates().setEventActive(false);
        coffeeRepository.saveAndFlush(deactivatedCoffeeEntity);
        //ID of turnOnJob is the Coffee Machine ID
        taskSchedulingService.removeScheduledTask(deactivatedCoffeeEntity.getId().toString());
        //Id of turnOffJob is the Coffee Machine UID (should also be unique)
        taskSchedulingService.removeScheduledTask(deactivatedCoffeeEntity.getDevice().getUid());
        return deactivatedCoffeeEntity;
    }

    public CoffeeEntity activateCoffeeEvent(CoffeeEntity coffeeEntity) {
        Optional<CoffeeEntity> coffeeEntityOptional = coffeeRepository.findByDevice_Uid(coffeeEntity.getDevice().getUid());
        if(coffeeEntityOptional.isEmpty()){
            throw new EntityNotFoundException("Coffee Device wasn't found!");
        }
        CoffeeEntity activatedCoffeeEntity = coffeeEntityOptional.get();
        activatedCoffeeEntity.getCoffeeStates().setEventActive(true);
        coffeeRepository.saveAndFlush(activatedCoffeeEntity);
        manageCoffeeTurnOn(activatedCoffeeEntity);
        manageCoffeeTurnOff(activatedCoffeeEntity);
        return activatedCoffeeEntity;
    }

    public CoffeeEntity deleteCoffeeDevice(CoffeeEntity coffeeEntity) {
        Optional<CoffeeEntity> coffeeEntityOptional = coffeeRepository.findByDevice_Uid(coffeeEntity.getDevice().getUid());
        if(coffeeEntityOptional.isEmpty()){
            throw new EntityNotFoundException("Coffee Device wasn't found!");
        }
        coffeeRepository.delete(coffeeEntityOptional.get());
        return coffeeEntityOptional.get();
    }

    public void setCoffeeDeviceToInactive(CoffeeEntity coffeeEntity) {
        Optional<CoffeeEntity> coffeeEntityOptional = coffeeRepository.findByDevice_Uid(coffeeEntity.getDevice().getUid());
        if(coffeeEntityOptional.isEmpty()){
            throw new EntityNotFoundException("Coffee Device wasn't found!");
        }
        CoffeeEntity deactivatedCoffeeEntity=coffeeEntityOptional.get();
        deactivatedCoffeeEntity.getCoffeeStates().setEventActive(false);
        coffeeRepository.save(deactivatedCoffeeEntity);
    }

    public CoffeeEntity findCoffeeMachine(CoffeeEntity coffeeEntity) {
        Optional<CoffeeEntity> coffeeEntityOptional = coffeeRepository.findByDevice_Uid(coffeeEntity.getDevice().getUid());
        if(coffeeEntityOptional.isEmpty()) {
            throw new EntityNotFoundException("We couldnt find your coffee device!");
        }
        return coffeeEntityOptional.get();
    }

    public void manageCoffeeTurnOn(CoffeeEntity coffeeEntity) {
        if (coffeeEntity.getCoffeeStates().isDailyAction()) {
            String dailyCronTurnOn = cronService.convertToDailyCron(coffeeEntity.getCoffeeStates().getDate());
            setCoffeeTimer(coffeeEntity, dailyCronTurnOn, "turnCoffeeOn");
        } else {
            String oneTimeCronTurnOn = cronService.convertDateToCron(coffeeEntity.getCoffeeStates().getDate());
            setCoffeeTimer(coffeeEntity, oneTimeCronTurnOn, "turnCoffeeOn");
        }
    }


    //this activates as soon as the timer for on goes Off within ScheduleService
    public void manageCoffeeTurnOff(CoffeeEntity coffeeEntity) {
        if (coffeeEntity.getCoffeeStates().isDailyAction()) {
            String dailyCronTurnOff = cronService.convertToDailyCron(coffeeEntity.getCoffeeStates().getDate().plus(1, ChronoUnit.HOURS));
            setCoffeeTimer(coffeeEntity, dailyCronTurnOff, "turnCoffeeOff");
        } else {
            String oneTimeCronTurnOff = cronService.convertDateToCron(coffeeEntity.getCoffeeStates().getDate().plus(1, ChronoUnit.HOURS));
            setCoffeeTimer(coffeeEntity, oneTimeCronTurnOff, "turnCoffeeOff");
        }
    }

    public void setCoffeeTimer(CoffeeEntity coffeeEntity, String cronCommand, String actionCommand) {

        if (actionCommand.equals("turnCoffeeOn")) {
            TaskDefinition taskDefinition = TaskDefinition.builder()
                    .actionType(actionCommand)
                    .cronExpression(cronCommand)
                    .coffeeEntity(coffeeEntity)
                    .date(coffeeEntity.getCoffeeStates().getDate()).build();
            scheduleService.setTaskDefinition(taskDefinition);
            taskSchedulingService.scheduleATask(coffeeEntity.getId().toString(), scheduleService, cronCommand);
        } else {
            TaskDefinition taskDefinition = TaskDefinition.builder()
                    .actionType(actionCommand)
                    .cronExpression(cronCommand)
                    .coffeeEntity(coffeeEntity)
                    .date(coffeeEntity.getCoffeeStates().getDate()).build();
            turnOffScheduleService.setTaskDefinition(taskDefinition);
            //doing this to get 2 different IDs, otherwise our event would get overwritten
            taskSchedulingService.scheduleATask(coffeeEntity.getDevice().getUid(), turnOffScheduleService, cronCommand);
        }
    }

    public List<CoffeeEntity> getCoffeeList(String userName) {
        return coffeeRepository.findAllByDevice_UserName(userName);
    }


    public List<CoffeeEntity> getAllCoffees() {
        return coffeeRepository.findAll();
    }

    public CoffeeEntity changeOnOffToOn(CoffeeEntity coffeeEntity) {
        CoffeeEntity foundCoffeeEntity = findCoffeeMachine(coffeeEntity);
        foundCoffeeEntity.getCoffeeStates().setOnOff(true);
        return coffeeRepository.save(foundCoffeeEntity);
    }

    public CoffeeEntity changeOnOffToOFF(CoffeeEntity coffeeEntity) {
        CoffeeEntity foundCoffeeEntity = findCoffeeMachine(coffeeEntity);
        foundCoffeeEntity.getCoffeeStates().setOnOff(false);
        return coffeeRepository.save(foundCoffeeEntity);
    }
}
