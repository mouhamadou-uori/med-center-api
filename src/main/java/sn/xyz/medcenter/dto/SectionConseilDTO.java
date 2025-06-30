package sn.xyz.medcenter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour les sections de conseil médical
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SectionConseilDTO {
    private Long id;
    private String titre;
    private String contenu;
    private Integer ordre;
    private Long conseilId;
}
