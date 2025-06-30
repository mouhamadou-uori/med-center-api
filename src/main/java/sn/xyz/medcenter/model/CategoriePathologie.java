package sn.xyz.medcenter.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Entité représentant une catégorie de pathologie
 */
@Entity
@Table(name = "categorie_pathologie")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoriePathologie {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nom;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    private String icone;
    
    private Integer ordre = 0;
    
    private Boolean active = true;
    
    @OneToMany(mappedBy = "categorie", cascade = CascadeType.ALL)
    private List<Pathologie> pathologies;
}
