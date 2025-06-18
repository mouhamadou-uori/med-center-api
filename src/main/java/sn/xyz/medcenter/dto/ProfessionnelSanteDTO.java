package sn.xyz.medcenter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO pour ProfessionnelSante, évitant les références circulaires et exposant les données utiles.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfessionnelSanteDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String lastName;
    private String firstName;
    private String username;
    private String email;
    private String tel;
    private String role;
    private LocalDateTime dateCreation;
    private Boolean actif;
    private String specialite;
    private String numeroOrdre;
    private HopitalShortDTO hopital;
}
