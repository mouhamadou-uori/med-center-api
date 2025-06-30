package sn.xyz.medcenter.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Entité représentant un conseil médical
 */
@Entity
@Table(name = "conseil_medical")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConseilMedical {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String titre;
    
    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String contenu;
    
    @Column(columnDefinition = "TEXT")
    private String resume;
    
    private String motsCles;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pathologie_id", nullable = false)
    private Pathologie pathologie;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auteur_id", nullable = false)
    private ProfessionnelSante auteur;
    
    @Column(nullable = false)
    private LocalDateTime dateCreation;
    
    @Column(nullable = false)
    private LocalDateTime dateModification;
    
    private LocalDateTime datePublication;
    
    @Enumerated(EnumType.STRING)
    private StatutConseil statut = StatutConseil.BROUILLON;
    
    private Boolean visiblePublic = false;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approuve_par_id")
    private ProfessionnelSante approuvePar;
    
    @OneToMany(mappedBy = "conseil", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SectionConseil> sections;
    
    @OneToMany(mappedBy = "conseil", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RessourceConseil> ressources;
    
    @OneToMany(mappedBy = "conseil", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Recommandation> recommandations;
    
    @PrePersist
    protected void onCreate() {
        this.dateCreation = LocalDateTime.now();
        this.dateModification = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        this.dateModification = LocalDateTime.now();
    }
    
    public enum StatutConseil {
        BROUILLON, EN_REVUE, PUBLIE, ARCHIVE
    }
}
