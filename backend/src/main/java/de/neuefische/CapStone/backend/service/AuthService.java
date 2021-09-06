package de.neuefische.CapStone.backend.service;

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
@Getter
@Setter
public class AuthService {

    private UserRepository userRepository;

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
        userRepository.save(userEntity);
        return userEntity;

    }

    private void checkIfEmailExists(String email) {
        Optional<UserEntity> existingEmail = userRepository.findByEmail(email);
        if(existingEmail.isPresent()){
            throw new EntityExistsException(String.format("Email with name %s already exists!", email));
        }
    }

    private void checkIfUserExists(String name) {
        Optional<UserEntity> existingUser = userRepository.findByUserName(name);
        if(existingUser.isPresent()){
            throw new EntityExistsException(String.format("User with name %s already exists!", name));
        }
    }
}
