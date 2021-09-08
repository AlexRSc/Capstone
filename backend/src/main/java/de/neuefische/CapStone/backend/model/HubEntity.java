package de.neuefische.CapStone.backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.*;

@Entity
@Table(name="smart_hub")
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class HubEntity {

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
