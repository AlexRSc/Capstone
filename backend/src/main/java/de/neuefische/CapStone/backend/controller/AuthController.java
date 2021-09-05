package de.neuefische.CapStone.backend.controller;

import de.neuefische.CapStone.backend.api.User;
import de.neuefische.CapStone.backend.model.UserEntity;
import de.neuefische.CapStone.backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@CrossOrigin
public class AuthController {

    private AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<User> registerNewUser(@RequestBody User user){
        UserEntity userEntity = map(user);
        UserEntity createdUserEntity = authService.create(userEntity);

    }

    private UserEntity map(User user) {
        UserEntity userEntity = new UserEntity();
        UserEntity.builder()
                .userName(user.getUserName())
                .email(user.getEmail())
                .password(user.getPassword()).build();
        return userEntity;
    }
}