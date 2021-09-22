package de.neuefische.CapStone.backend.controller;


import de.neuefische.CapStone.backend.api.Alarm;
import de.neuefische.CapStone.backend.model.*;
import de.neuefische.CapStone.backend.service.AlarmService;
import de.neuefische.CapStone.backend.service.HubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.util.StringUtils.hasText;


@RestController
@CrossOrigin
@RequestMapping("/alarm")
public class AlarmController {

    private final HubService hubService;
    private final AlarmService alarmService;

    @Autowired
    public AlarmController(HubService hubService, AlarmService alarmService) {
        this.hubService = hubService;
        this.alarmService = alarmService;
    }

    @PostMapping("/create")
    public ResponseEntity<Alarm> createAlarmDevice(@AuthenticationPrincipal UserEntity authUser, @RequestBody Alarm alarm) {
        if(!hasText(alarm.getDeviceName())) {
            throw new IllegalArgumentException("DeviceName is blank!");
        }
        if(!hasText(alarm.getUid())) {
            throw new IllegalArgumentException("UId is blank!");
        }
        AlarmEntity alarmEntity = map(alarm, authUser);
        HubEntity hubEntity = hubService.findHubByUserName(authUser.getUserName());
        AlarmEntity createdAlarmEntity = alarmService.createAlarmDevice(alarmEntity, hubEntity);
        Alarm createdAlarm = map(createdAlarmEntity);
        return ok(createdAlarm);
    }

    @PutMapping("/setEvent")
    public ResponseEntity<Alarm> setAlarmDeviceEvent(@AuthenticationPrincipal UserEntity authUser, @RequestBody Alarm alarm) {
        return null;
    }

    @PutMapping("/cancelEvent")
    public ResponseEntity<Alarm> cancelAlarmDeviceEvent(@AuthenticationPrincipal UserEntity authUser, @RequestBody Alarm alarm) {
        return null;
    }

    @PutMapping("/setVolume")
    public ResponseEntity<Alarm> setAlarmDeviceVolume(@AuthenticationPrincipal UserEntity authUser, @RequestBody Alarm alarm) {
        return null;
    }

    @PutMapping("/turnOn")
    public ResponseEntity<Alarm> turnAlarmDeviceOn(@AuthenticationPrincipal UserEntity authUser, @RequestBody Alarm alarm) {
        return null;
    }

    @PutMapping("/turnOff")
    public ResponseEntity<Alarm> turnAlarmDeviceOFF(@AuthenticationPrincipal UserEntity authUser, @RequestBody Alarm alarm) {
        return null;
    }

    private Alarm map(AlarmEntity createdAlarmEntity) {
        return Alarm.builder()
                .deviceName(createdAlarmEntity.getAlarmDevice().getDeviceName())
                .uid(createdAlarmEntity.getAlarmDevice().getUid())
                .onOff(createdAlarmEntity.getAlarmStates().isOnOff())
                .volume(createdAlarmEntity.getAlarmStates().getVolume()).build();
    }

    private AlarmEntity map(Alarm alarm, UserEntity authUser) {
        AlarmDevice alarmDevice = AlarmDevice.builder()
                .deviceName(alarm.getDeviceName())
                .uid(alarm.getUid())
                .userName(authUser.getUserName()).build();
        AlarmStates alarmStates = AlarmStates.builder()
                .onOff(alarm.isOnOff())
                .volume(alarm.getVolume())
                .build();
        return AlarmEntity.builder()
                .alarmDevice(alarmDevice)
                .alarmStates(alarmStates).build();
    }
}
