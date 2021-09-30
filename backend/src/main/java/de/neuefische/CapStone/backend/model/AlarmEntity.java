package de.neuefische.CapStone.backend.model;

import lombok.*;

import javax.persistence.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

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

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER, mappedBy = "id")
    private final Set<AlarmEventEntity> alarmEventEntities = new HashSet<>();

    public Set<AlarmEventEntity> getAlarmEventEntities() {
        return Collections.unmodifiableSet(alarmEventEntities);
    }

    public void setEvent(AlarmEventEntity alarmEventEntity) {
        this.alarmEventEntities.add(alarmEventEntity);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AlarmEntity that = (AlarmEntity) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
