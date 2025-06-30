package sn.xyz.medcenter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO pour les pathologies
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PathologieDTO {
    private Long id;
    private String nom;
    private String slug;
    private String description;
    private String icone;
    private Long categorieId;
    private String categorieNom;
    private Integer ordre;
    private Boolean publiee;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Liste des conseils médicaux associés (version simplifiée)
    private List<ConseilMedicalShortDTO> conseils;
    
    // Liste des pathologies associées (version simplifiée)
    private List<PathologieAssocieeDTO> pathologiesAssociees;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ConseilMedicalShortDTO {
        private Long id;
        private String titre;
        private String statut;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PathologieAssocieeDTO {
        private Long id;
        private String nom;
        private String slug;
    }
}
