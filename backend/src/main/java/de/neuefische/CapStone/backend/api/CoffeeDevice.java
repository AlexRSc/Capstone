package de.neuefische.CapStone.backend.api;

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
public class CoffeeDevice {

    private String deviceName;
    private Date date;
    private String uid;
    private String itemName;
    private boolean onOff;
}
