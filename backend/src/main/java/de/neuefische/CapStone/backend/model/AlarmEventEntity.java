package de.neuefische.CapStone.backend.model;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "alarm_event")
@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class AlarmEventEntity {

    @ManyToOne
    @JoinColumn(name = "alarm_id", nullable = false)
    private AlarmEntity alarmEntity;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name="is_event")
    boolean isEvent;

    @Column(name="is_daily")
    boolean isDaily;

    @Column(name="date")
    Instant date;


}
