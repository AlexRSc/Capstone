package de.neuefische.CapStone.backend.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name="lights_device")
@SecondaryTable(name="device_states", pkJoinColumns = @PrimaryKeyJoinColumn(name= "lights_device_id"))
@SecondaryTable(name="device", pkJoinColumns = @PrimaryKeyJoinColumn(name= "lights_device_id"))
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class LightsDevice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Embedded
    @AttributeOverrides(value={
            @AttributeOverride(name="userName", column=@Column(name="lights_userName")),
            @AttributeOverride(name="uid", column = @Column(name="lights_uid")),
            @AttributeOverride(name="itemName", column=@Column(name="lights_item_name")),
            @AttributeOverride(name="deviceName", column = @Column(name="device_name")),
            @AttributeOverride(name="hubId", column=@Column(name="hub_id")),
    })
    private Device device;


    @Embedded
    @AttributeOverrides(value={
            @AttributeOverride(name="brightness", column=@Column(name="brightness")),
            @AttributeOverride(name="onOff", column = @Column(name="on_off"))
    })
    private DeviceStates deviceStates;
}
