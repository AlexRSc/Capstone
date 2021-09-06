package de.neuefische.CapStone.backend.controller;

import de.neuefische.CapStone.backend.api.User;
import de.neuefische.CapStone.backend.model.UserEntity;
import de.neuefische.CapStone.backend.repo.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.swing.*;

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

    private String url() { return "http://localhost:" + port;}

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestRestTemplate testRestTemplate;

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

}