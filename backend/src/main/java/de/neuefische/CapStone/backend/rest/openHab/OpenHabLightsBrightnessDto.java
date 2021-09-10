package de.neuefische.CapStone.backend.rest.openHab;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OpenHabLightsBrightnessDto {

    String itemName;
    String brightnessLevel;
}
