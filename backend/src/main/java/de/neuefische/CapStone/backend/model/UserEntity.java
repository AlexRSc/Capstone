package de.neuefische.CapStone.backend.model;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="smart_user")
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch =FetchType.EAGER, mappedBy = "id")
    private final Set<HubEntity> hubEntitySet = new HashSet<>();

    @Id
    @GeneratedValue
    @Column(name="id", nullable = false)
    private Long id;

    @Column(name="user_name", nullable = false, unique = true)
    private String userName;

    @Column(name="user_email", nullable = false, unique = true)
    private String email;

    @Column(name="password", nullable = false)
    private String password;

    @Column(name="role")
    private String role;
}
