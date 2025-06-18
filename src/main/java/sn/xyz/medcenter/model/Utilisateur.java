package sn.xyz.medcenter.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "utilisateur")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Inheritance(strategy = InheritanceType.JOINED)
public class Utilisateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    private String tel;

    @Column(nullable = false)
    private String password;

    @Column(name = "roles")
    private String role;

    // Les rôles sont gérés uniquement via la relation many-to-many rolesSet

    @Column(name = "date_creation", nullable = false)
    private LocalDateTime dateCreation;

    @Column(name = "date_suppression")
    private LocalDateTime dateSuppression;

    @Builder.Default
    private Boolean actif = true;

}
