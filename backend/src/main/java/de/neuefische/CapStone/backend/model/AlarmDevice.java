package de.neuefische.CapStone.backend.model;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Builder
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class AlarmDevice {

    @Column(name = "user_name")
    private String userName;

    @Column(name = "uid", unique = true)
    private String uid;

    @Column(name = "device_name")
    private String deviceName;

    @Column(name = "media_item")
    private String mediaItem;

    @Column(name = "volume_item")
    private String volumeItem;

    @Column(name = "shuffle_item")
    private String shuffleItem;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER, mappedBy = "id")
    private final Set<AlarmEventEntity> alarmEventEntities = new HashSet<>();
}
