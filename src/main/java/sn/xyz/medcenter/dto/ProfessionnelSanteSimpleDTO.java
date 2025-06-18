package sn.xyz.medcenter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO simplifié pour ProfessionnelSante, utilisé dans les listes pour éviter les références circulaires.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfessionnelSanteSimpleDTO implements Serializable {
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
    private String etablissement;
    private String region;
}
