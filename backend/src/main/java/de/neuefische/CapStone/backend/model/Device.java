package de.neuefische.CapStone.backend.model;


import javax.persistence.Column;
import javax.persistence.Embeddable;

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
    @Column(name="hub_id")
    private String hubId;
}
