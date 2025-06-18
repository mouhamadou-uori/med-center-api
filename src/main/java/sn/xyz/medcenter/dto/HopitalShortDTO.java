package sn.xyz.medcenter.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HopitalShortDTO {
    private Integer id;
    private String nom;
    private String type;
    private String region;
    private String ville;
    private String adresse;
    private String telephone;
}
