package de.neuefische.CapStone.backend.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LightDevice {

    private String deviceName;
    private String uid;
    private String itemName;
    private boolean onOff;
    private String brightness;

}
