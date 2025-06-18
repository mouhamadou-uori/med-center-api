package sn.xyz.medcenter.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Entity
@Table(name = "patient")
@PrimaryKeyJoinColumn(name = "id")
@EqualsAndHashCode(callSuper = false)
public class Patient extends Utilisateur {
    @Column(name = "numero_secu", nullable = false, unique = true)
    private String numeroSecu;

    private String adresse;
    private String contactUrgence;

    @OneToOne(mappedBy = "patient", cascade = CascadeType.ALL)
    private DossierMedical dossierMedical;

    @OneToMany(mappedBy = "patient")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Consultation> consultations;

    @OneToMany(mappedBy = "patient")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<PathologieChronique> pathologies;
}

