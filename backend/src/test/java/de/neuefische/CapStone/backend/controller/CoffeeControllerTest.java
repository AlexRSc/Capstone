package de.neuefische.CapStone.backend.controller;

import de.neuefische.CapStone.backend.api.CoffeeDevice;
import de.neuefische.CapStone.backend.api.OnOffDevice;
import de.neuefische.CapStone.backend.config.JwtConfig;
import de.neuefische.CapStone.backend.model.*;
import de.neuefische.CapStone.backend.repo.CoffeeRepository;
import de.neuefische.CapStone.backend.repo.HubRepository;
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

import java.sql.Date;
import java.time.Duration;
import java.time.Instant;
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
class CoffeeControllerTest {

    @LocalServerPort
    private int port;

    private String url() {return "http://localhost:" + port;}

    private final UserRepository userRepository;
    private final HubRepository hubRepository;
    private final CoffeeRepository coffeeRepository;
    private final JwtConfig jwtConfig;
    private final TestRestTemplate testRestTemplate;

    @Autowired
    CoffeeControllerTest(UserRepository userRepository, HubRepository hubRepository, CoffeeRepository coffeeRepository, JwtConfig jwtConfig, TestRestTemplate testRestTemplate) {
        this.userRepository = userRepository;
        this.hubRepository = hubRepository;
        this.coffeeRepository = coffeeRepository;
        this.jwtConfig = jwtConfig;
        this.testRestTemplate = testRestTemplate;
    }

    @AfterEach
    public void clearRepos() {
        coffeeRepository.deleteAll();
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
    public void AddNewCoffeeMachine() {
        //GIVEN
        CoffeeDevice coffeeDeviceToAdd = CoffeeDevice.builder()
                .deviceName("light")
                .uid("uniqueID")
                .itemName("groupURL")
                .date(Date.from(Instant.now()))
                .onOff(false).build();

        String url = url() + "/coffee/add";

        //WHEN
        HttpEntity<CoffeeDevice> httpEntity = new HttpEntity<>(coffeeDeviceToAdd,
                authorizedHeader("test","user", "test@test.com"));

        ResponseEntity<CoffeeDevice> response = testRestTemplate.exchange(url, HttpMethod.POST, httpEntity, CoffeeDevice.class);

        //THEN
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), notNullValue());
        assertThat(response.getBody().getUid(), is(coffeeDeviceToAdd.getUid()));
    }

    @Test
    public void tryToAddNewCoffeeDeviceAlreadyInDB() {
        //GIVEN
        Optional<HubEntity> hubEntityOptional= hubRepository.findHubEntityByUserName("test");
        if(hubEntityOptional.isEmpty()){
            fail();
        }
        HubEntity hub = hubEntityOptional.get();
        coffeeRepository.saveAndFlush(CoffeeEntity.builder()
                .hubEntity(hub)
                .device(Device.builder()
                        .deviceName("light")
                        .uid("uniqueID")
                        .itemName("groupURL")
                        .userName("test").build())
                .coffeeStates(CoffeeStates.builder()
                        .date(Instant.now())
                        .onOff(false).build()).build());

        CoffeeDevice coffeeDeviceToAdd = CoffeeDevice.builder()
                .deviceName("light")
                .uid("uniqueID")
                .itemName("groupURL")
                .date(Date.from(Instant.now()))
                .onOff(false).build();

        String url = url() + "/coffee/add";

        //WHEN
        HttpEntity<CoffeeDevice> httpEntity = new HttpEntity<>(coffeeDeviceToAdd,
                authorizedHeader("test","user", "test@test.com"));

        ResponseEntity<CoffeeDevice> response = testRestTemplate.exchange(url, HttpMethod.POST, httpEntity, CoffeeDevice.class);

        //THEN
        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));

    }

    @Test
    public void tryToAddCoffeeWithEmptyParameters() {
        //GIVEN
        CoffeeDevice coffeeDeviceToAdd = CoffeeDevice.builder()
                .deviceName("light")
                .uid("")
                .itemName("groupURL")
                .date(Date.from(Instant.now()))
                .onOff(false).build();

        String url = url() + "/coffee/add";

        //WHEN
        HttpEntity<CoffeeDevice> httpEntity = new HttpEntity<>(coffeeDeviceToAdd,
                authorizedHeader("test","user", "test@test.com"));

        ResponseEntity<CoffeeDevice> response = testRestTemplate.exchange(url, HttpMethod.POST, httpEntity, CoffeeDevice.class);

        //THEN
        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));

    }

    @Test
    public void unauthorizedUserTriesToAddCoffeeDevice() {
        //GIVEN
        CoffeeDevice coffeeDeviceToAdd = CoffeeDevice.builder()
                .deviceName("light")
                .uid("unique_id")
                .itemName("groupURL")
                .date(Date.from(Instant.now()))
                .onOff(false).build();

        String url = url() + "/coffee/add";

        //WHEN
        HttpEntity<CoffeeDevice> httpEntity = new HttpEntity<>(coffeeDeviceToAdd);

        ResponseEntity<CoffeeDevice> response = testRestTemplate.exchange(url, HttpMethod.POST, httpEntity, CoffeeDevice.class);

        //THEN
        assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));

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