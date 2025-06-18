package sn.xyz.medcenter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO pour transférer les données d'un patient sans les références circulaires
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientDTO {
    private Integer id;
    private String lastName;
    private String firstName;
    private String username;
    private String email;
    private String tel;
    private String numeroSecu;
    private String adresse;
    private String contactUrgence;
    private LocalDateTime dateCreation;
    private Boolean actif;
    
    // ID du dossier médical associé (sans l'objet complet)
    private Integer dossierMedicalId;
    
    // Nombre de consultations (sans les objets complets)
    private Integer nombreConsultations;
}
