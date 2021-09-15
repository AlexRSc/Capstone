package de.neuefische.CapStone.backend.schedulingTask;

import de.neuefische.CapStone.backend.api.CoffeeDevice;
import de.neuefische.CapStone.backend.model.CoffeeEntity;
import de.neuefische.CapStone.backend.model.Device;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskDefinition {
    private String cronExpression;
    private String actionType;
    private CoffeeEntity coffeeEntity;
    private Instant date;
}
