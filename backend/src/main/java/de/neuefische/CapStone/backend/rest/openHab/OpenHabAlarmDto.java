package de.neuefische.CapStone.backend.rest.openHab;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OpenHabAlarmDto {

    private String itemName;
    private String command;

}
