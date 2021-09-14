package de.neuefische.CapStone.backend.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Setter
@Getter
@Builder
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class LightsDeviceStates {
    @Column(name="brightness")
    String brightness;

    @Column(name="on_off")
    boolean onOff;

}
