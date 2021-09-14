package de.neuefische.CapStone.backend.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name="lights_device")
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class LightsDeviceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="hub_id", nullable = false)
    private HubEntity hubEntity;

    @Embedded
    @AttributeOverrides(value={
            @AttributeOverride(name="userName", column=@Column(name="lights_user_name")),
            @AttributeOverride(name="uid", column = @Column(name="lights_uid")),
            @AttributeOverride(name="itemName", column=@Column(name="lights_item_name")),
            @AttributeOverride(name="deviceName", column = @Column(name="device_name")),
    })
    private Device device;


    @Embedded
    @AttributeOverrides(value={
            @AttributeOverride(name="brightness", column=@Column(name="brightness")),
            @AttributeOverride(name="onOff", column = @Column(name="on_off"))
    })
    private LightsDeviceStates lightsDeviceStates;
}
