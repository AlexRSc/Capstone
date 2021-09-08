package de.neuefische.CapStone.backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Embeddable
public class DeviceStates {
    @Column(name="brightness")
    boolean brightness;
    @Column(name="on_off")
    boolean onOff;
    @Column(name="timer")
    Long timer;
}
