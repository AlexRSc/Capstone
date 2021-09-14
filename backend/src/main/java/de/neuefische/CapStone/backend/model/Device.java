package de.neuefische.CapStone.backend.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Embeddable
public class Device {
    @Column(name= "user_name")
    private String userName;
    @Column(name="uid")
    private String uid;
    @Column(name="item_name")
    private String itemName;
    @Column(name="device_name")
    private String deviceName;

}
