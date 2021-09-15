package de.neuefische.CapStone.backend.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.Instant;
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
    Instant date;

    @Column(name="daily_action")
    boolean dailyAction;

}