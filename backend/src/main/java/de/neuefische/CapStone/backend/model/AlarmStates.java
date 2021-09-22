package de.neuefische.CapStone.backend.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.Instant;

@Setter
@Getter
@Builder
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class AlarmStates {

    @Column(name="on_off")
    boolean onOff;

    @Column(name="volume")
    String volume;

}
