package sn.xyz.medcenter.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Entity
@Table(name = "parametre_suivi")
@NoArgsConstructor
@AllArgsConstructor
public class ParametreSuivi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String nom;

    private String unite;
    private Float valeurMin;
    private Float valeurMax;
    private String frequenceMesure;
    private Boolean alerteActive = false;

    @ManyToOne
    @JoinColumn(name = "pathologie_id", nullable = false)
    private PathologieChronique pathologie;

    @OneToMany(mappedBy = "parametre")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<DonneeSante> donneesSante;
}