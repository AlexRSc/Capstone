package de.neuefische.CapStone.backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="smart_hub")
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class HubEntity {

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private UserEntity userEntity;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch =FetchType.EAGER, mappedBy = "id")
    private final Set<LightsDeviceEntity> lightsDeviceEntities = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch =FetchType.EAGER, mappedBy = "id")
    private final Set<CoffeeEntity> coffeeEntitySet = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch =FetchType.EAGER, mappedBy = "id")
    private final Set<OnOffDeviceEntity> onOffDeviceEntities = new HashSet<>();

    @Id
    @GeneratedValue
    @Column(name="id", nullable = false)
    private Long id;

    @Column(name="hub_email", nullable = false, unique = true)
    private String hubEmail;

    @Column(name="hub_password", nullable = false)
    private String hubPassword;

    @Column(name="user_name", nullable = false)
    private String userName;

    @Column(name="hub_state")
    private String hubState;

}
