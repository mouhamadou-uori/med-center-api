package sn.xyz.medcenter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour les ressources associées à un conseil médical
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RessourceConseilDTO {
    private Long id;
    private String titre;
    private String type;
    private String url;
    private String description;
    private Long conseilId;
}
