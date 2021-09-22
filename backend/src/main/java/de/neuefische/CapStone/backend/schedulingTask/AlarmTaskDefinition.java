package de.neuefische.CapStone.backend.schedulingTask;

import de.neuefische.CapStone.backend.model.AlarmEventEntity;
import de.neuefische.CapStone.backend.model.CoffeeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlarmTaskDefinition {
    private String cronExpression;
    private String actionType;
    private AlarmEventEntity alarmEventEntity;
    private Instant date;
}
