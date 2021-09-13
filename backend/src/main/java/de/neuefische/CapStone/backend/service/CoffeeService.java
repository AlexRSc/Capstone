package de.neuefische.CapStone.backend.service;

import de.neuefische.CapStone.backend.model.CoffeeEntity;
import de.neuefische.CapStone.backend.model.HubEntity;
import de.neuefische.CapStone.backend.repo.CoffeeRepository;
import de.neuefische.CapStone.backend.repo.HubRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CoffeeService {

    private final CoffeeRepository coffeeRepository;
    private final HubRepository hubRepository;

    public CoffeeService(CoffeeRepository coffeeRepository, HubRepository hubRepository) {
        this.coffeeRepository = coffeeRepository;
        this.hubRepository = hubRepository;
    }

    public CoffeeEntity create(CoffeeEntity coffeeEntity) {
        Optional<HubEntity> hubEntityOptional= hubRepository.findHubEntityByUserName(coffeeEntity.getDevice().getUserName());
        if (hubEntityOptional.isEmpty()){
            throw new IllegalArgumentException("You don´t have a Hub");
        }
        Optional<CoffeeEntity> coffeeEntityOptional = coffeeRepository.findByDevice_Uid(coffeeEntity.getDevice().getUid());
        if(coffeeEntityOptional.isPresent()){
            throw new IllegalArgumentException("You already have that Coffee Maker!");
        }
        coffeeEntity.getDevice().setHubId(hubEntityOptional.get().getId());
        return coffeeRepository.save(coffeeEntity);
    }
}
