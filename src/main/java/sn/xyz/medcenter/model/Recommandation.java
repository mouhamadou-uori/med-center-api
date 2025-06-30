package sn.xyz.medcenter.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entité représentant une recommandation associée à un conseil médical
 */
@Entity
@Table(name = "recommandation")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Recommandation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String texte;
    
    @Column(nullable = false)
    private Integer ordre;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conseil_id", nullable = false)
    private ConseilMedical conseil;
}
