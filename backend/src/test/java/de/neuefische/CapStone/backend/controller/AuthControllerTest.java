package de.neuefische.CapStone.backend.controller;

import de.neuefische.CapStone.backend.api.AccessToken;
import de.neuefische.CapStone.backend.api.Credentials;
import de.neuefische.CapStone.backend.api.User;
import de.neuefische.CapStone.backend.config.JwtConfig;
import de.neuefische.CapStone.backend.model.UserEntity;
import de.neuefische.CapStone.backend.repo.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(
        properties = "spring.profiles.active:h2",
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class AuthControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    AuthControllerTest(UserRepository userRepository, PasswordEncoder passwordEncoder, TestRestTemplate testRestTemplate, JwtConfig jwtConfig) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.testRestTemplate = testRestTemplate;
        this.jwtConfig = jwtConfig;
    }

    private String url() { return "http://localhost:" + port;}

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TestRestTemplate testRestTemplate;
    private final JwtConfig jwtConfig;

    @AfterEach
    public void clearUserRepo() {userRepository.deleteAll();}

    @Test
    public void createNewUser() {
        //GIVEN
        User userToAdd = User.builder()
                .userName("Bowser")
                .email("Bowser@supermario.com")
                .password("ImInLoveWithMario").build();
        String newUrl = url() + "/user/registration";
        //WHEN
        HttpEntity<User> httpEntity = new HttpEntity<>(userToAdd);
        ResponseEntity<User> response = testRestTemplate.exchange(newUrl, HttpMethod.POST, httpEntity, User.class);
        Optional<UserEntity> checkIfUserIsInDB = userRepository.findByUserName(userToAdd.getUserName());
        //THEN
        assertThat(checkIfUserIsInDB.isPresent(), is(notNullValue()));
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody().getUserName(), is(checkIfUserIsInDB.get().getUserName()));
        assertThat(response.getBody().getEmail(), is(checkIfUserIsInDB.get().getEmail()));
    }
    @Test
    public void RegisterUserThatAlreadyExists() {
        //GIVEN
        UserEntity user = UserEntity.builder()
                .userName("Bowser")
                .email("Bowser@com")
                .password("ImInLoveWithMario").build();
        userRepository.saveAndFlush(user);
        User userToAdd = User.builder()
                .userName("Bowser")
                .email("Bowser@supermario.com")
                .password("ImInLoveWithMario").build();
        String newUrl = url() + "/user/registration";
        //WHEN
        HttpEntity<User> httpEntity = new HttpEntity<>(userToAdd);
        ResponseEntity<User> response = testRestTemplate.exchange(newUrl, HttpMethod.POST, httpEntity, User.class);
        //THEN
        assertThat(response.getStatusCode(), is(HttpStatus.CONFLICT));
    }
    @Test
    public void registerEmailThatAlreadyExists() {
        //GIVEN
        UserEntity user = UserEntity.builder()
                .userName("Mario")
                .email("Bowser@supermario.com")
                .password("ImInLoveWithMario").build();
        userRepository.saveAndFlush(user);
        User userToAdd = User.builder()
                .userName("Bowser")
                .email("Bowser@supermario.com")
                .password("ImInLoveWithMario").build();
        String newUrl = url() + "/user/registration";
        //WHEN
        HttpEntity<User> httpEntity = new HttpEntity<>(userToAdd);
        ResponseEntity<User> response = testRestTemplate.exchange(newUrl, HttpMethod.POST, httpEntity, User.class);
        //THEN
        assertThat(response.getStatusCode(), is(HttpStatus.CONFLICT));
    }

    @Test
    public void userLogsIn() {
        //GIVEN
        String encodedPassword= passwordEncoder.encode("password");
        String userName = "Bowser";
        String email = "Bowser@supermario.com";
        String role = "user";
        String password = "password";
        userRepository.save(
                UserEntity.builder()
                        .userName(userName)
                        .email(email)
                        .role(role)
                        .password(encodedPassword).build()
        );

        Credentials credentials = Credentials.builder()
                .userName(userName)
                .password(password).build();
        String newUrl = url() + "/user/login";

        //WHEN
      //  HttpEntity<Credentials> httpEntity = new HttpEntity<>(credentials);
        ResponseEntity<AccessToken> response = testRestTemplate.postForEntity(newUrl, credentials, AccessToken.class);

        //THEN
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        String token = response.getBody().getToken();
        Claims claims = Jwts.parser().setSigningKey(jwtConfig.getSecret()).parseClaimsJws(token).getBody();
        assertThat(claims.getSubject(), is(userName));
        assertThat(claims.get("email", String.class), is(email));

    }
    @Test
    public void unregisteredUserTriesToLogin() {
        //GIVEN

        Credentials credentials = Credentials.builder()
                .userName("userName")
                .password("password").build();
        String newUrl = url() + "/user/login";

        //WHEN
      //  HttpEntity<Credentials> httpEntity = new HttpEntity<>(credentials);
        ResponseEntity<AccessToken> response = testRestTemplate.postForEntity(newUrl, credentials, AccessToken.class);

        //THEN
        assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
    }

    @Test
    public void userTriesToLoginWithWrongPassword() {
        //GIVEN
        String encodedPassword= passwordEncoder.encode("password");
        String userName = "Bowser";
        String email = "Bowser@supermario.com";
        String role = "user";
        String password = "wrongpassword";
        userRepository.save(
                UserEntity.builder()
                        .userName(userName)
                        .email(email)
                        .role(role)
                        .password(encodedPassword).build()
        );

        Credentials credentials = Credentials.builder()
                .userName(userName)
                .password(password).build();
        String newUrl = url() + "/user/login";

        //WHEN
        //  HttpEntity<Credentials> httpEntity = new HttpEntity<>(credentials);
        ResponseEntity<AccessToken> response = testRestTemplate.postForEntity(newUrl, credentials, AccessToken.class);

        //THEN
        assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
    }

}