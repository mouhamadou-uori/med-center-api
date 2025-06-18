package sn.xyz.medcenter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO pour transférer les données d'une consultation sans les références circulaires
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsultationDTO {
    private Integer id;
    private LocalDateTime dateHeure;
    private String type;
    private String statut;
    private String notes;
    
    // Informations sur le professionnel de santé
    private Integer professionnelId;
    private String professionnelNom;
    private String professionnelPrenom;
    private String professionnelSpecialite;
    
    // Informations sur le patient
    private Integer patientId;
    private String patientNom;
    private String patientPrenom;
    
    // Nombre de prescriptions associées
    private Integer nombrePrescriptions;
}
