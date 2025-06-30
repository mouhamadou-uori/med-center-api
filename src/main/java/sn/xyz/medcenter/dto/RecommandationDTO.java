package sn.xyz.medcenter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour les recommandations associées à un conseil médical
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecommandationDTO {
    private Long id;
    private String texte;
    private Integer ordre;
    private Long conseilId;
}
