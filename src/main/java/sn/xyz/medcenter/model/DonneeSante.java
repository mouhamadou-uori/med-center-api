package sn.xyz.medcenter.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "donnee_sante")
@NoArgsConstructor
@AllArgsConstructor
public class DonneeSante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Float valeur;

    @Column(name = "date_enregistrement", nullable = false)
    private LocalDateTime dateEnregistrement;

    private String source;
    private Boolean valide = false;

    @ManyToOne
    @JoinColumn(name = "parametre_id", nullable = false)
    private ParametreSuivi parametre;
}
