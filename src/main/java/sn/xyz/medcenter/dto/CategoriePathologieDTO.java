package sn.xyz.medcenter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO pour les catégories de pathologies
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoriePathologieDTO {
    private Long id;
    private String nom;
    private String description;
    private String icone;
    private Integer ordre;
    private Boolean active;
    
    // Liste de pathologies pour éviter les références circulaires
    private List<PathologieShortDTO> pathologies;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PathologieShortDTO {
        private Long id;
        private String nom;
        private String slug;
    }
}
