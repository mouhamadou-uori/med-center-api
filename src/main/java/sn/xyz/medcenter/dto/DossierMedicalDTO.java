package sn.xyz.medcenter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO pour transférer les données d'un dossier médical sans les références circulaires
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DossierMedicalDTO {
    private Integer id;
    private LocalDateTime dateCreation;
    private LocalDateTime dateMiseAJour;
    private Integer patientId;
    private String patientNom;
    private String patientPrenom;
}
