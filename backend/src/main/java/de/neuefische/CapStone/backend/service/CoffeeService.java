package de.neuefische.CapStone.backend.service;

import de.neuefische.CapStone.backend.model.CoffeeEntity;
import de.neuefische.CapStone.backend.model.HubEntity;
import de.neuefische.CapStone.backend.repo.CoffeeRepository;
import de.neuefische.CapStone.backend.repo.HubRepository;
import de.neuefische.CapStone.backend.schedulingTask.ScheduleService;
import de.neuefische.CapStone.backend.schedulingTask.TaskDefinitionTest;
import de.neuefische.CapStone.backend.schedulingTask.TaskSchedulingService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class CoffeeService {

    private final CoffeeRepository coffeeRepository;
    private final HubRepository hubRepository;
    private final ScheduleService scheduleService;
    private final TaskSchedulingService taskSchedulingService;
    private final CronService cronService;

    public CoffeeService(CoffeeRepository coffeeRepository, HubRepository hubRepository, ScheduleService scheduleService, TaskSchedulingService taskSchedulingService, CronService cronService) {
        this.coffeeRepository = coffeeRepository;
        this.hubRepository = hubRepository;
        this.scheduleService = scheduleService;
        this.taskSchedulingService = taskSchedulingService;
        this.cronService = cronService;
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
        coffeeRepository.saveAndFlush(coffeeEntity);
        Optional<CoffeeEntity> createdCoffeeEntityOptional=coffeeRepository.findByDevice_Uid(coffeeEntity.getDevice().getUid());
        if(createdCoffeeEntityOptional.isPresent()){
            setCoffeeTimer(createdCoffeeEntityOptional.get());
        } else{
            throw new IllegalArgumentException("Oops, something went wrong!");
        }
        return coffeeEntity;
    }

    public void setCoffeeTimer(CoffeeEntity coffeeEntity) {
        TaskDefinitionTest taskDefinitionTest = TaskDefinitionTest.builder()
                .actionType("Set CoffeeMachine")
                .cronExpression(cronService.convertDateToCron(coffeeEntity.getCoffeeStates().getDate()))
                .device(coffeeEntity.getDevice())
                .date(coffeeEntity.getCoffeeStates().getDate()).build();
        scheduleService.setTaskDefinition(taskDefinitionTest);
        taskSchedulingService.scheduleATask(coffeeEntity.getId().toString(), scheduleService, cronService.convertDateToCron(taskDefinitionTest.getDate()));
    }

    public List<CoffeeEntity> getCoffeeList(String userName) {
        return coffeeRepository.findAllByDevice_UserName(userName);
    }
}
