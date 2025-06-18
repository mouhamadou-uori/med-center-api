package sn.xyz.medcenter.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "pathologie_chronique")
@NoArgsConstructor
@AllArgsConstructor
public class PathologieChronique {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String nom;

    private String description;

    @Column(name = "date_detection", nullable = false)
    private LocalDate dateDetection;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @OneToMany(mappedBy = "pathologie", cascade = CascadeType.ALL)
    private List<ParametreSuivi> parametresSuivi;
}