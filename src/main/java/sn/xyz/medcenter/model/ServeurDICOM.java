package sn.xyz.medcenter.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "serveur_dicom")
@NoArgsConstructor
@AllArgsConstructor
public class ServeurDICOM {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "url_orthanc", nullable = false)
    private String urlOrthanc;

    @Column(name = "port_orthanc", nullable = false)
    private String portOrthanc;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @ManyToOne
    @JoinColumn(name = "hopital_id", nullable = false)
    private Hopital hopital;
}
