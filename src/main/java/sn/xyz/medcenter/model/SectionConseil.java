package sn.xyz.medcenter.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entité représentant une section de conseil médical
 */
@Entity
@Table(name = "section_conseil")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SectionConseil {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String titre;
    
    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String contenu;
    
    @Column(nullable = false)
    private Integer ordre;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conseil_id", nullable = false)
    private ConseilMedical conseil;
}
