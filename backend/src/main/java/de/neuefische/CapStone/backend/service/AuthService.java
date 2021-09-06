package de.neuefische.CapStone.backend.service;

import de.neuefische.CapStone.backend.api.User;
import de.neuefische.CapStone.backend.model.UserEntity;
import de.neuefische.CapStone.backend.repo.UserRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import java.util.Optional;

import static org.springframework.util.StringUtils.hasText;

@Service
public class AuthService {

    private final UserRepository userRepository;

    @Autowired
    public AuthService(UserRepository userRepository){
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
