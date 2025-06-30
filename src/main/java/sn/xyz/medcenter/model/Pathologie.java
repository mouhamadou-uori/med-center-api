package sn.xyz.medcenter.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Entité représentant une pathologie
 */
@Entity
@Table(name = "pathologie")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pathologie {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nom;
    
    @Column(nullable = false, unique = true)
    private String slug;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    private String icone;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categorie_id", nullable = false)
    private CategoriePathologie categorie;
    
    private Integer ordre = 0;
    
    private Boolean publiee = false;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "pathologie", cascade = CascadeType.ALL)
    private List<ConseilMedical> conseils;
    
    @ManyToMany
    @JoinTable(
        name = "relation_pathologies",
        joinColumns = @JoinColumn(name = "pathologie_source_id"),
        inverseJoinColumns = @JoinColumn(name = "pathologie_cible_id")
    )
    private List<Pathologie> pathologiesAssociees;
    
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
