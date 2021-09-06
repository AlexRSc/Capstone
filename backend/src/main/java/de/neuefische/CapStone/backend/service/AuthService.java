package de.neuefische.CapStone.backend.service;

import de.neuefische.CapStone.backend.model.UserEntity;
import de.neuefische.CapStone.backend.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;

import static org.springframework.util.StringUtils.hasText;

@Service
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Autowired
    public AuthService(PasswordEncoder passwordEncoder, UserRepository userRepository){
        this.passwordEncoder = passwordEncoder;
        this.userRepository=userRepository;
    }

    public UserEntity createUser(UserEntity userEntity) {
        String name = userEntity.getUserName();
        if(!hasText(name)) {
            throw new IllegalArgumentException("Name must not be blank");
        }
        checkIfUserExists(name);
        String email = userEntity.getEmail();
        if(!hasText(email)) {
            throw new IllegalArgumentException("Email must not be blank");
        }
        checkIfEmailExists(email);
        userEntity.setRole("user");
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        return userRepository.save(userEntity);
    }

    private void checkIfEmailExists(String email) {
        Boolean emailAlreadyExists = userRepository.existsByEmail(email);
        if(emailAlreadyExists){
            throw new EntityExistsException(String.format("Email with name %s already exists!", email));
        }
    }

    private void checkIfUserExists(String userName) {
        Boolean userNameAlreadyExists = userRepository.existsByUserName(userName);
        if(userNameAlreadyExists){
            throw new EntityExistsException(String.format("User with name %s already exists!", userName));
        }
    }
}
