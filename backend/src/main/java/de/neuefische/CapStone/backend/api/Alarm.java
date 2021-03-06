package de.neuefische.CapStone.backend.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Alarm {

    private String deviceName;
    private String uid;
    private boolean onOff;
    private String volume;
    private String mediaItem;
    private String volumeItem;
}
