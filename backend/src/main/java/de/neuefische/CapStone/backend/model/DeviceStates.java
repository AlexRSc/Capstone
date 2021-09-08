package de.neuefische.CapStone.backend.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class DeviceStates {
    @Column(name="brightness")
    String brightness;
    @Column(name="on_off")
    String onOff;
    @Column(name="timer")
    Long timer;
}
