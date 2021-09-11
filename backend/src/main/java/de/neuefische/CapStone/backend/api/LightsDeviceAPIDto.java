package de.neuefische.CapStone.backend.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LightsDeviceAPIDto {

    private String deviceName;
    private String uid;
    private String itemName;

}
