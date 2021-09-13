package de.neuefische.CapStone.backend.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Date;

@Setter
@Getter
@Builder
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class CoffeeStates {

    @Column(name="on_off")
    boolean onOff;

    @Column(name="date")
    Date date;

}