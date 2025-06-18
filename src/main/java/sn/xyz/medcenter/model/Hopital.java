package sn.xyz.medcenter.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "hopital")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Hopital {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String nom;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeHopital type;

    @Column(nullable = false)
    private String region;

    @Column(nullable = false)
    private String ville;

    @Column(nullable = false)
    private String adresse;

    @Column(nullable = false)
    private String telephone;

    private String email;
    private String siteWeb;
    private String statut;

    @OneToMany(mappedBy = "hopital")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<ProfessionnelSante> professionnels;

    @OneToMany(mappedBy = "hopital")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<ServeurDICOM> serveursDICOM;
}

