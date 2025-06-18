package sn.xyz.medcenter.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "consultation")
@NoArgsConstructor
@AllArgsConstructor
public class Consultation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "date_heure", nullable = false)
    private LocalDateTime dateHeure;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String statut;

    private String notes;

    @ManyToOne
    @JoinColumn(name = "professionnel_id", nullable = false)
    private ProfessionnelSante professionnel;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @OneToMany(mappedBy = "consultation")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Prescription> prescriptions;
}