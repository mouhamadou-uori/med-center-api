package sn.xyz.medcenter.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entité représentant une ressource associée à un conseil médical
 */
@Entity
@Table(name = "ressource_conseil")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RessourceConseil {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String titre;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeRessource type;
    
    @Column(nullable = false)
    private String url;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conseil_id", nullable = false)
    private ConseilMedical conseil;
    
    public enum TypeRessource {
        LIEN, PDF, IMAGE, VIDEO
    }
}
