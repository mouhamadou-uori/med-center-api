package sn.xyz.medcenter.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Entity
@Table(name = "professionnel_sante")
@PrimaryKeyJoinColumn(name = "id")
@EqualsAndHashCode(callSuper = false)
public class ProfessionnelSante extends Utilisateur {
    private String specialite;
    private String numeroOrdre;
    private String etablissement;
    private String region;

    @ManyToOne
    @JoinColumn(name = "hopital_id")
    private Hopital hopital;

    @OneToMany(mappedBy = "professionnel")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Consultation> consultations;
}