package de.neuefische.CapStone.backend.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="alarm_entity")
@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class AlarmEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="hub_id", nullable = false)
    private HubEntity hubEntity;

    @Embedded
    private AlarmDevice alarmDevice;

    @Embedded
    private AlarmStates alarmStates;
}
