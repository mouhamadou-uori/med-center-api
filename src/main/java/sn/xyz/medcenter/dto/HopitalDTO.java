package sn.xyz.medcenter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * DTO pour Hopital, évitant les références circulaires et exposant les données utiles.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HopitalDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String nom;
    private String type;
    private String region;
    private String ville;
    private String adresse;
    private String telephone;
    private String email;
    private String siteWeb;
    private String statut;
    
    // Liste simplifiée des professionnels (sans les détails de l'hôpital pour éviter la circularité)
    private List<ProfessionnelSanteSimpleDTO> professionnels;
}
