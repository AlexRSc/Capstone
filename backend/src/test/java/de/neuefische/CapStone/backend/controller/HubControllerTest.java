package de.neuefische.CapStone.backend.controller;

import de.neuefische.CapStone.backend.api.Hub;
import de.neuefische.CapStone.backend.config.JwtConfig;
import de.neuefische.CapStone.backend.model.HubEntity;
import de.neuefische.CapStone.backend.model.UserEntity;
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
import org.springframework.security.crypto.password.PasswordEncoder;

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
public class HubControllerTest {

    @LocalServerPort
    private int port;

    private final HubRepository hubRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TestRestTemplate testRestTemplate;
    private final JwtConfig jwtConfig;

    @Autowired
    public HubControllerTest(HubRepository hubRepository, UserRepository userRepository, PasswordEncoder passwordEncoder, TestRestTemplate testRestTemplate, JwtConfig jwtConfig) {
        this.hubRepository = hubRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.testRestTemplate = testRestTemplate;
        this.jwtConfig = jwtConfig;
    }

    private String url() {
        return "http://localhost:" + port;
    }

    @AfterEach
    public void clearUserRepo() {
        hubRepository.deleteAll();
        userRepository.deleteAll();
    }

    @BeforeEach
    public void setUser() {
        userRepository.save(UserEntity.builder()
                .userName("test")
                .password(passwordEncoder.encode("password"))
                .email("test@test.com")
                .role("user")
                .build());
    }

    @Test
    public void connectNewHub() {
        //GIVEN
        HubEntity hubToAdd = HubEntity.builder()
                .hubEmail("hubEmail@email.com")
                .hubPassword("hubPassword").build();
        String url = url() + "/hub/newHub";
        //WHEN
        HttpEntity<HubEntity> httpEntity = new HttpEntity<>(hubToAdd, authorizedHeader("test", "user", "test@test.com")) ;
        ResponseEntity<Hub> response = testRestTemplate.exchange(url, HttpMethod.POST, httpEntity, Hub.class);

        //THEN
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), notNullValue());
        assertThat(response.getBody().getHubEmail(), is(hubToAdd.getHubEmail()));
    }

    @Test
    public void hubAlreadyExists() {
        //GIVEN
        Optional<UserEntity> userEntityOptional = userRepository.findByUserName("test");
        if(userEntityOptional.isEmpty()){
            fail();
        }
        UserEntity userEntity = userEntityOptional.get();
        HubEntity hubInDB = HubEntity.builder()
                .userName("test")
                .hubEmail("hubEmail@email.com")
                .hubPassword("hubPassword")
                .userEntity(userEntity).build();
        hubRepository.saveAndFlush(hubInDB);
        HubEntity hubToAdd = HubEntity.builder()
                .hubEmail("hubEmail@email.com")
                .hubPassword("hubPassword").build();
        String url = url() + "/hub/newHub";
        //WHEN
        HttpEntity<HubEntity> httpEntity = new HttpEntity<>(hubToAdd, authorizedHeader("test", "user", "test@test.com")) ;
        ResponseEntity<Hub> response = testRestTemplate.exchange(url, HttpMethod.POST, httpEntity, Hub.class);

        //THEN
        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
    }
    @Test
    public void hubDoesntAcceptEmptyInput() {
        //GIVEN
        HubEntity hubToAdd = HubEntity.builder()
                .hubEmail("")
                .hubPassword("hubPassword").build();
        String url = url() + "/hub/newHub";
        //WHEN
        HttpEntity<HubEntity> httpEntity = new HttpEntity<>(hubToAdd, authorizedHeader("test", "user", "test@test.com")) ;
        ResponseEntity<Hub> response = testRestTemplate.exchange(url, HttpMethod.POST, httpEntity, Hub.class);

        //THEN
        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
    }
    @Test
    public void hubDoesntAcceptNULLInput() {
        //GIVEN
        HubEntity hubToAdd = HubEntity.builder()
                .hubEmail(null)
                .hubPassword("hubPassword").build();
        String url = url() + "/hub/newHub";
        //WHEN
        HttpEntity<HubEntity> httpEntity = new HttpEntity<>(hubToAdd, authorizedHeader("test", "user", "test@test.com")) ;
        ResponseEntity<Hub> response = testRestTemplate.exchange(url, HttpMethod.POST, httpEntity, Hub.class);

        //THEN
        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
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
