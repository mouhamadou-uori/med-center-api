package sn.xyz.medcenter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO pour les conseils médicaux
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConseilMedicalDTO {
    private Long id;
    private String titre;
    private String contenu;
    private String resume;
    private String motsCles;
    private Long pathologieId;
    private String pathologieNom;
    private Long auteurId;
    private String auteurNom;
    private LocalDateTime dateCreation;
    private LocalDateTime dateModification;
    private LocalDateTime datePublication;
    private String statut;
    private Boolean visiblePublic;
    private Long approuveParId;
    private String approuveParNom;
    
    // Listes des éléments associés
    private List<SectionConseilDTO> sections;
    private List<RessourceConseilDTO> ressources;
    private List<RecommandationDTO> recommandations;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SectionConseilDTO {
        private Long id;
        private String titre;
        private String contenu;
        private Integer ordre;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RessourceConseilDTO {
        private Long id;
        private String titre;
        private String type;
        private String url;
        private String description;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecommandationDTO {
        private Long id;
        private String texte;
        private Integer ordre;
    }
}
