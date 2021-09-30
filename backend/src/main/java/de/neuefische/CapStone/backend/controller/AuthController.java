package de.neuefische.CapStone.backend.controller;

import de.neuefische.CapStone.backend.api.AccessToken;
import de.neuefische.CapStone.backend.api.Credentials;
import de.neuefische.CapStone.backend.api.User;
import de.neuefische.CapStone.backend.model.UserEntity;
import de.neuefische.CapStone.backend.service.AuthService;
import de.neuefische.CapStone.backend.service.JwtService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import static io.jsonwebtoken.lang.Strings.hasText;
import static org.springframework.http.ResponseEntity.ok;

@Api(
        tags = AuthController.AUTH_CONTROLLER_TAG
)
@RestController
@CrossOrigin
@RequestMapping("/user")
public class AuthController {

    public static final String AUTH_CONTROLLER_TAG = "Auth";
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Autowired
    public AuthController(AuthService authService, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/registration")
    public ResponseEntity<User> registerNewUser(@RequestBody User user){
        UserEntity userEntity = map(user);

        UserEntity createdUserEntity = authService.createUser(userEntity);
        User createdUser = map(createdUserEntity);
        return ok(createdUser);

    }

    @PostMapping("/login")
    public ResponseEntity<AccessToken> login(@RequestBody Credentials credentials){
        String userName = credentials.getUserName();
        if(!hasText(userName)){
            throw new IllegalArgumentException("Username can´t be blank!");
        }
        String password = credentials.getPassword();
        if(!hasText(password)){
            throw new IllegalArgumentException("Password can´t be blank!");
        }
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userName, password);

        try {
            authenticationManager.authenticate(authToken);
            UserEntity user = authService.find(userName).orElseThrow();
            String token = jwtService.createJwtToken(user);
            AccessToken accessToken = new AccessToken(token);
            return ok(accessToken);
        } catch (AuthenticationException ex) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

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