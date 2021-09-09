package de.neuefische.CapStone.backend.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@NoArgsConstructor
public class OpenhabClientConfigProperties {

    private String username;
    private String password;

    public OpenhabClientConfigProperties(String username, String password){
        this.username = username;
        this.password = password;
    }

    public OpenhabClientConfigProperties getClientConfigProperties() {
        return new OpenhabClientConfigProperties(getUsername(), getPassword());
    }
}
