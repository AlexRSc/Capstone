package de.neuefische.CapStone.backend.controller;

import de.neuefische.CapStone.backend.api.Alarm;
import de.neuefische.CapStone.backend.api.AlarmEvent;
import de.neuefische.CapStone.backend.config.JwtConfig;
import de.neuefische.CapStone.backend.model.*;
import de.neuefische.CapStone.backend.repo.AlarmEventRepository;
import de.neuefische.CapStone.backend.repo.AlarmRepository;
import de.neuefische.CapStone.backend.repo.HubRepository;
import de.neuefische.CapStone.backend.repo.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.hibernate.dialect.PostgreSQL10Dialect;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.fail;


@SpringBootTest(
        properties = "spring.profiles.active:h2",
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class AlarmControllerTest {


    @LocalServerPort
    private int port;

    private String url() {
        return "http://localhost:" + port;
    }

    private final UserRepository userRepository;
    private final HubRepository hubRepository;
    private final JwtConfig jwtConfig;
    private final TestRestTemplate testRestTemplate;
    private final AlarmRepository alarmRepository;
    private final AlarmEventRepository alarmEventRepository;

    @Autowired
    AlarmControllerTest(UserRepository userRepository, HubRepository hubRepository, JwtConfig jwtConfig, TestRestTemplate testRestTemplate, AlarmRepository alarmRepository, AlarmEventRepository alarmEventRepository) {
        this.userRepository = userRepository;
        this.hubRepository = hubRepository;
        this.jwtConfig = jwtConfig;
        this.testRestTemplate = testRestTemplate;
        this.alarmRepository = alarmRepository;
        this.alarmEventRepository = alarmEventRepository;
    }

    @AfterEach
    public void clearRepos() {
        alarmEventRepository.deleteAllInBatch();
        alarmRepository.deleteAllInBatch();
        hubRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @BeforeEach
    public void setRepos() {
        UserEntity user = UserEntity.builder()
                .userName("test")
                .password("password")
                .email("test@test.com")
                .role("user")
                .build();
        HubEntity hubEntity = HubEntity.builder()
                .hubEmail("testy")
                .hubPassword("password")
                .userName("test")
                .userEntity(user).build();
        AlarmEntity alarmEntity = AlarmEntity.builder()
                .alarmDevice(AlarmDevice.builder()
                        .deviceName("testAlarm")
                        .mediaItem("testMediaItem")
                        .shuffleItem("testShuffleItem")
                        .volumeItem("testVolumeItem")
                        .userName("test")
                        .uid("RandomUID")
                        .build())
                .alarmStates(AlarmStates.builder()
                        .onOff(true)
                        .volume("100")
                        .build()).build();
        AlarmEventEntity alarmEventEntity1 = AlarmEventEntity.builder()
                .id(1L)
                .isEvent(true)
                .isDaily(true)
                .date(Instant.now())
                .build();
        AlarmEventEntity alarmEventEntity2 = AlarmEventEntity.builder()
                .isEvent(true)
                .isDaily(true)
                .date(Instant.now().plus(5, ChronoUnit.MINUTES))
                .build();
        UserEntity userEntity = userRepository.saveAndFlush(user);
        hubEntity.setUserEntity(userEntity);
        HubEntity savedHubEntity = hubRepository.saveAndFlush(hubEntity);
        alarmEntity.setHubEntity(savedHubEntity);
        AlarmEntity savedAlarmEntity = alarmRepository.saveAndFlush(alarmEntity);
        alarmEventEntity1.setAlarmEntity(savedAlarmEntity);
        alarmEventEntity2.setAlarmEntity(savedAlarmEntity);
        alarmEventRepository.saveAndFlush(alarmEventEntity1);
        alarmEventRepository.save(alarmEventEntity2);

    }

    @Test
    public void testGetAlarmsMethod() {
        //GIVEN
        String url = url() + "/alarm/getMyAlarms";
        //WHEN
        HttpEntity<?> httpEntity = new HttpEntity<>(null, authorizedHeader("test", "user", "test@test.com"));
        ResponseEntity<Alarm[]> response= testRestTemplate.exchange(url, HttpMethod.GET, httpEntity, Alarm[].class);
        //THEN
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(Objects.requireNonNull(response.getBody()).length, is(1));
        Alarm alarm = response.getBody()[0];
        assertThat(alarm.getUid(), is("RandomUID"));
    }

    @Test
    public void testGetAllMyEvents() {
        //GIVEN
        String url = url() + "/alarm/getMyEvents/RandomUID";
        //WHEN
        HttpEntity<?> httpEntity = new HttpEntity<>(null, authorizedHeader("test", "user", "test@test.com"));
        ResponseEntity<AlarmEvent[]> response= testRestTemplate.exchange(url, HttpMethod.GET, httpEntity, AlarmEvent[].class);
        //THEN
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(Objects.requireNonNull(response.getBody()).length, is(2));
    }

    @Test
    public void userCreatesAlarmDevice() {
        //GIVEN
        String url = url() + "/alarm/create";
        Alarm alarm = Alarm.builder()
                .deviceName("testAlarmDevice")
                .uid("testUIDasas")
                .mediaItem("randomMedia")
                .volumeItem("randomVolume").build();
        //WHEN
        HttpEntity<Alarm> httpEntity = new HttpEntity<>(alarm, authorizedHeader("test", "user", "test@test.com"));
        ResponseEntity<Alarm> response= testRestTemplate.exchange(url, HttpMethod.POST, httpEntity, Alarm.class);
        //THEN
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(Objects.requireNonNull(response.getBody()).getUid(), is("testUIDasas"));
    }

    @Test
    public void UnauthorizedTriesToCreateAlarmDevice() {
        //GIVEN
        String url = url() + "/alarm/create";
        Alarm alarm = Alarm.builder()
                .deviceName("testAlarmDevice")
                .uid("testUIDasas")
                .mediaItem("randomMedia")
                .volumeItem("randomVolume").build();
        //WHEN
        HttpEntity<Alarm> httpEntity = new HttpEntity<>(alarm);
        ResponseEntity<Alarm> response= testRestTemplate.exchange(url, HttpMethod.POST, httpEntity, Alarm.class);
        //THEN
        assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
    }

    @Test
    public void userCreatesAlarmDevicewithoutUID() {
        //GIVEN
        String url = url() + "/alarm/create";
        Alarm alarm = Alarm.builder()
                .deviceName("testAlarmDevice")
                .uid("")
                .mediaItem("randomMedia")
                .volumeItem("randomVolume").build();
        //WHEN
        HttpEntity<Alarm> httpEntity = new HttpEntity<>(alarm, authorizedHeader("test", "user", "test@test.com"));
        ResponseEntity<Alarm> response= testRestTemplate.exchange(url, HttpMethod.POST, httpEntity, Alarm.class);
        //THEN
        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
    }

    @Test
    public void userCreatesAlarmEvent() {
        //GIVEN
        String url = url() + "/alarm/setEvent/RandomUID";
        AlarmEvent alarmEvent = AlarmEvent.builder()
                .date(Date.from(Instant.now()))
                .isEvent(true)
                .isDaily(true).build();
        //WHEN
        HttpEntity<AlarmEvent> httpEntity = new HttpEntity<>(alarmEvent, authorizedHeader("test", "user", "test@test.com"));
        ResponseEntity<AlarmEvent> response= testRestTemplate.exchange(url, HttpMethod.POST, httpEntity, AlarmEvent.class);
        //THEN
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(Objects.requireNonNull(response.getBody()).isEvent(), is(true));
    }

    @Test
    public void UnauthorizedUserTriesToCreateAlarmEvent() {
        //GIVEN
        String url = url() + "/alarm/setEvent/RandomUID";
        AlarmEvent alarmEvent = AlarmEvent.builder()
                .date(Date.from(Instant.now()))
                .isEvent(true)
                .isDaily(true).build();
        //WHEN
        HttpEntity<AlarmEvent> httpEntity = new HttpEntity<>(alarmEvent);
        ResponseEntity<AlarmEvent> response= testRestTemplate.exchange(url, HttpMethod.POST, httpEntity, AlarmEvent.class);
        //THEN
        assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
    }

    @Test
    public void userDeletesNonExistingAlarmEvent() {
        //GIVEN
        String url = url() + "/alarm/deleteAlarmEvent/5";
        //WHEN
        HttpEntity<?> httpEntity = new HttpEntity<>(authorizedHeader("test", "user", "test@test.com"));
        ResponseEntity<AlarmEvent> response= testRestTemplate.exchange(url, HttpMethod.DELETE, httpEntity, AlarmEvent.class);
        //THEN
        assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }

    private HttpHeaders authorizedHeader(String userName, String role, String email) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        claims.put("email", email);
        Instant now = Instant.now();
        java.util.Date iat = java.util.Date.from(now);
        java.util.Date exp = java.util.Date.from(now.plus(Duration.ofMinutes(jwtConfig.getExpiresAfterMinutes())));
        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject(userName)
                .setIssuedAt(iat)
                .setExpiration(exp)
                .signWith(SignatureAlgorithm.HS256, jwtConfig.getSecret())
                .compact();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return headers;
    }
}