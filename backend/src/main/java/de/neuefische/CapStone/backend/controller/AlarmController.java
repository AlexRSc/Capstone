package de.neuefische.CapStone.backend.controller;


import de.neuefische.CapStone.backend.api.Alarm;
import de.neuefische.CapStone.backend.api.AlarmEvent;
import de.neuefische.CapStone.backend.model.*;
import de.neuefische.CapStone.backend.service.AlarmEventService;
import de.neuefische.CapStone.backend.service.AlarmService;
import de.neuefische.CapStone.backend.service.HubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.util.StringUtils.hasText;


@RestController
@CrossOrigin
@RequestMapping("/alarm")
public class AlarmController {

    private final HubService hubService;
    private final AlarmService alarmService;
    private final AlarmEventService alarmEventService;

    @Autowired
    public AlarmController(HubService hubService, AlarmService alarmService, AlarmEventService alarmEventService) {
        this.hubService = hubService;
        this.alarmService = alarmService;
        this.alarmEventService = alarmEventService;
    }

    @GetMapping("/getMyAlarms")
    public ResponseEntity<List<Alarm>> getMyAlarms(@AuthenticationPrincipal UserEntity authUser) {
        List<AlarmEntity> alarmEntityList = alarmService.findAllAlarmsByUsername(authUser.getUserName());
        return ok(map(alarmEntityList));
    }


    @GetMapping("/getMyEvents/{uid}")
    public ResponseEntity<List<AlarmEvent>> getMyEventsByAlarmDevice(@PathVariable String uid) {
        AlarmEntity alarmEntity = alarmService.findAlarmEntity(uid);
        List<AlarmEventEntity> alarmEventEntityList = alarmEventService.findAllAlarmEventsByAlarmDevice(alarmEntity);
        //sadly I cannot map 2 Lists within 1 file, might need to make another Controller for this? or just outsource maping
        //one maping file for AlarmDevices, one maping file for AlarmEvents
        List<AlarmEvent> alarmEventList = new LinkedList<>();
        for (AlarmEventEntity alarmEventEntityFromList : alarmEventEntityList) {
            AlarmEvent alarmEventFromList = map(alarmEventEntityFromList);
            alarmEventList.add(alarmEventFromList);
        }
        return ok(alarmEventList);
    }

    @PostMapping("/create")
    public ResponseEntity<Alarm> createAlarmDevice(@AuthenticationPrincipal UserEntity authUser, @RequestBody Alarm alarm) {
        if (!hasText(alarm.getDeviceName())) {
            throw new IllegalArgumentException("DeviceName is blank!");
        }
        if (!hasText(alarm.getUid())) {
            throw new IllegalArgumentException("UId is blank!");
        }
        AlarmEntity alarmEntity = map(alarm, authUser);
        HubEntity hubEntity = hubService.findHubByUserName(authUser.getUserName());
        AlarmEntity createdAlarmEntity = alarmService.createAlarmDevice(alarmEntity, hubEntity);
        Alarm createdAlarm = map(createdAlarmEntity);
        return ok(createdAlarm);
    }

    @PostMapping("/setEvent/{uid}")
    public ResponseEntity<AlarmEvent> setAlarmDeviceEvent(@AuthenticationPrincipal UserEntity authUser, @RequestBody AlarmEvent alarmEvent,
                                                          @PathVariable String uid) {

        AlarmEntity alarmEntity = alarmService.findAlarmEntity(uid);
        AlarmEventEntity alarmEventEntity = alarmEventService.setEvent(map(alarmEvent), alarmEntity);
        AlarmEvent createdAlarmEvent = map(alarmEventEntity);
        return ok(createdAlarmEvent);
    }


    @PutMapping("/activateEvent")
    public ResponseEntity<AlarmEvent> activateEvent(@RequestBody AlarmEvent alarmEvent) {
        AlarmEventEntity alarmEventEntity = alarmEventService.activateAlarmEvent(map(alarmEvent));
        return ok(map(alarmEventEntity));
    }

    @PutMapping("/cancelEvent")
    public ResponseEntity<AlarmEvent> cancelAlarmDeviceEvent(@RequestBody AlarmEvent alarmEvent) {
        AlarmEventEntity alarmEventEntity = alarmEventService.deactivateAlarmEvent(map(alarmEvent));
        return ok(map(alarmEventEntity));
    }

    @PutMapping("/setVolume/{uid}")
    public ResponseEntity<Alarm> setAlarmDeviceVolume(@PathVariable String uid, @RequestBody String volume) {
        AlarmEntity alarmEntity = alarmService.findAlarmEntity(uid);
        AlarmEntity newAlarmEntity = alarmService.setAlarmEntityVolume(alarmEntity, volume);
        return ok(map(newAlarmEntity));

    }

    @PutMapping("/turnOn")
    public ResponseEntity<Alarm> turnAlarmDeviceOn(@RequestBody Alarm alarm) {
        AlarmEntity alarmEntity = alarmService.findAlarmEntity(alarm.getUid());
        AlarmEntity turnedOnAlarmEntity = alarmService.turnOnAlarmDevice(alarmEntity);
        return ok(map(turnedOnAlarmEntity));
    }

    @PutMapping("/turnOff")
    public ResponseEntity<Alarm> turnAlarmDeviceOFF(@RequestBody Alarm alarm) {
        AlarmEntity alarmEntity = alarmService.findAlarmEntity(alarm.getUid());
        AlarmEntity turnedOffAlarmEntity = alarmService.turnOffAlarmDevice(alarmEntity);
        return ok(map(turnedOffAlarmEntity));
    }

    private List<Alarm> map(List<AlarmEntity> alarmEntityList) {
        List<Alarm> alarmList = new LinkedList<>();
        for (AlarmEntity alarmEntity : alarmEntityList) {
            Alarm alarm = map(alarmEntity);
            alarmList.add(alarm);
        }
        return alarmList;
    }

    private AlarmEventEntity map(AlarmEvent alarmEvent) {
        return AlarmEventEntity.builder()
                .id(alarmEvent.getId())
                .date(alarmEvent.getDate().toInstant())
                .isDaily(alarmEvent.isDaily())
                .isEvent(alarmEvent.isEvent())
                .build();
    }

    private AlarmEvent map(AlarmEventEntity alarmEventEntity) {
        return AlarmEvent.builder()
                .id(alarmEventEntity.getId())
                .date(Date.from(alarmEventEntity.getDate()))
                .isDaily(alarmEventEntity.isDaily())
                .isEvent(alarmEventEntity.isEvent())
                .build();
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
