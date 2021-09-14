package de.neuefische.CapStone.backend.schedulingTask;

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
public class TaskDefinitionTest {
    private String cronExpression;
    private String actionType;
    private Device device;
    private Instant date;
}
