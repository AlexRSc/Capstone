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
public class OnOffDeviceStates {

    @Column(name="on_off")
    boolean onOff;

}
