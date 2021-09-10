package de.neuefische.CapStone.backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name="on_off_device")
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class OnOffDeviceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Embedded
    @AttributeOverrides(value={
            @AttributeOverride(name="userName", column=@Column(name="user_name")),
            @AttributeOverride(name="uid", column = @Column(name="uid")),
            @AttributeOverride(name="itemName", column=@Column(name="item_name")),
            @AttributeOverride(name="deviceName", column = @Column(name="device_name")),
            @AttributeOverride(name="hubId", column=@Column(name="hub_id")),
    })
    private Device device;


    @Embedded
    @AttributeOverrides(value={
            @AttributeOverride(name="onOff", column = @Column(name="on_off"))
    })
    private OnOffDeviceStates onOffDeviceStates;
}
