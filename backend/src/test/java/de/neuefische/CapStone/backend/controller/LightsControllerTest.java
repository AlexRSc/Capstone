package de.neuefische.CapStone.backend.controller;

import de.neuefische.CapStone.backend.api.LightDevice;
import de.neuefische.CapStone.backend.config.JwtConfig;
import de.neuefische.CapStone.backend.model.*;
import de.neuefische.CapStone.backend.repo.HubRepository;
import de.neuefische.CapStone.backend.repo.LightsRepository;
import de.neuefische.CapStone.backend.repo.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest(
        properties = "spring.profiles.active:h2",
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class LightsControllerTest {


    @LocalServerPort
    private int port;

    private String url() {return "http://localhost:" + port;}

    private final UserRepository userRepository;
    private final HubRepository hubRepository;
    private final LightsRepository lightsRepository;
    private final JwtConfig jwtConfig;
    private final TestRestTemplate testRestTemplate;

    @Autowired
    public LightsControllerTest(UserRepository userRepository, HubRepository hubRepository, LightsRepository lightsRepository, JwtConfig jwtConfig, TestRestTemplate testRestTemplate) {
        this.userRepository = userRepository;
        this.hubRepository = hubRepository;
        this.lightsRepository = lightsRepository;
        this.jwtConfig = jwtConfig;
        this.testRestTemplate = testRestTemplate;
    }


    @AfterEach
    public void clearRepos() {
        lightsRepository.deleteAll();
        hubRepository.deleteAll();
        userRepository.deleteAll();
    }

    @BeforeEach
    public void setUserAndHub() {
        UserEntity user = UserEntity.builder()
                .userName("test")
                .password("password")
                .email("test@test.com")
                .role("user")
                .build();
        userRepository.saveAndFlush(user);
        hubRepository.save(HubEntity.builder()
                .hubEmail("testy")
                .hubPassword("password")
                .userName("test")
                .userEntity(user).build());
    }

    @Test
    public void AddNewLightTest() {
        //GIVEN
        LightDevice lightDeviceToAdd = LightDevice.builder()
                .deviceName("light")
                .uid("uniqueID")
                .itemName("groupURL")
                .onOff(false)
                .brightness("0").build();

        String url = url() + "/lights/add";

        //WHEN
        HttpEntity<LightDevice> httpEntity = new HttpEntity<>(lightDeviceToAdd,
                authorizedHeader("test","user", "test@test.com"));

        ResponseEntity<LightDevice> response = testRestTemplate.exchange(url, HttpMethod.POST, httpEntity, LightDevice.class);

        //THEN
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), notNullValue());
        assertThat(response.getBody().getUid(), is(lightDeviceToAdd.getUid()));
    }

    @Test
    public void AddLightsDeviceIsAlreadyInDB() {
        //GIVEN
        Optional<HubEntity> hubEntityOptional= hubRepository.findHubEntityByUserName("test");
        if(hubEntityOptional.isEmpty()){
            fail();
        }
        HubEntity hub = hubEntityOptional.get();
        lightsRepository.saveAndFlush(LightsDeviceEntity.builder()
                .hubEntity(hub)
                .device(Device.builder()
                        .deviceName("light")
                        .uid("uniqueID")
                        .itemName("groupURL")
                        .userName("test").build())
                .lightsDeviceStates(LightsDeviceStates.builder()
                        .brightness("0")
                        .onOff(false).build()).build());

        LightDevice lightDeviceToAdd = LightDevice.builder()
                .deviceName("light")
                .uid("uniqueID")
                .itemName("groupURL")
                .onOff(false)
                .brightness("0").build();

        String url = url() + "/lights/add";

        //WHEN
        HttpEntity<LightDevice> httpEntity = new HttpEntity<>(lightDeviceToAdd,
                authorizedHeader("test","user", "test@test.com"));

        ResponseEntity<LightDevice> response = testRestTemplate.exchange(url, HttpMethod.POST, httpEntity, LightDevice.class);

        //THEN
        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
    }

    @Test
    public void AddLightsDeviceWithEmptyParameters() {
        //GIVEN
        LightDevice lightDeviceToAdd = LightDevice.builder()
                .deviceName("light")
                .uid("")
                .itemName("groupURL")
                .onOff(false)
                .brightness("0").build();

        String url = url() + "/lights/add";

        //WHEN
        HttpEntity<LightDevice> httpEntity = new HttpEntity<>(lightDeviceToAdd,
                authorizedHeader("test","user", "test@test.com"));

        ResponseEntity<LightDevice> response = testRestTemplate.exchange(url, HttpMethod.POST, httpEntity, LightDevice.class);

        //THEN
        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
    }

    @Test
    public void unauthorizedUserTriesToAddLight() {
        //GIVEN
        LightDevice lightDeviceToAdd = LightDevice.builder()
                .deviceName("light")
                .uid("unique_id")
                .itemName("groupURL")
                .onOff(false)
                .brightness("0").build();

        String url = url() + "/lights/add";

        //WHEN
        HttpEntity<LightDevice> httpEntity = new HttpEntity<>(lightDeviceToAdd);

        ResponseEntity<LightDevice> response = testRestTemplate.exchange(url, HttpMethod.POST, httpEntity, LightDevice.class);

        //THEN
        assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
    }

    private HttpHeaders authorizedHeader(String userName, String role, String email) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        claims.put("email", email);
        Instant now = Instant.now();
        Date iat = Date.from(now);
        Date exp = Date.from(now.plus(Duration.ofMinutes(jwtConfig.getExpiresAfterMinutes())));
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