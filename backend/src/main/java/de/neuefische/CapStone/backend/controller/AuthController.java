package de.neuefische.CapStone.backend.controller;

import de.neuefische.CapStone.backend.api.User;
import de.neuefische.CapStone.backend.model.UserEntity;
import de.neuefische.CapStone.backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/registration")
    public ResponseEntity<User> registerNewUser(@RequestBody User user){
        UserEntity userEntity = map(user);
        UserEntity createdUserEntity = authService.createUser(userEntity);
        User createdUser=map(createdUserEntity);
        return ok(createdUser);
    }

    private UserEntity map(User user) {
        return UserEntity.builder()
                .userName(user.getUserName())
                .email(user.getEmail())
                .password(user.getPassword()).build();
    }

    private User map(UserEntity userEntity) {
        return User.builder()
                .userName(userEntity.getUserName())
                .email(userEntity.getEmail())
                .password(userEntity.getPassword()).build();
    }
}