package sn.xyz.medcenter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO pour transférer les données d'une prescription sans les références circulaires
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionDTO {
    private Integer id;
    private LocalDate dateEmission;
    private LocalDate dateExpiration;
    private String medicaments;
    private String instructions;
    private Boolean renouvelable;
    
    // ID de la consultation associée
    private Integer consultationId;
}
