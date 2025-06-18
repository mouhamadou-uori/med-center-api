package sn.xyz.medcenter.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "fichier")
@NoArgsConstructor
@AllArgsConstructor
public class Fichier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private Integer taille;

    @Column(nullable = false)
    private String chemin;

    @ManyToOne
    @JoinColumn(name = "message_id", nullable = false)
    private Message message;
}
