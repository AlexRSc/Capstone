package de.neuefische.CapStone.backend.rest.openHab;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OpenHabOnOffDto {

    private String itemName;
    private String onOff;

}
