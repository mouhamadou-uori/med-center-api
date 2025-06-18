package sn.xyz.medcenter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * DTO pour retourner les patients Orthanc avec leurs détails
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrthancPatientsResponseDTO {
    private String orthancUrl;
    private Integer hopitalId;
    private String hopitalNom;
    private List<OrthancPatientDetailDTO> patients;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrthancPatientDetailDTO {
        private String id;
        private String patientName;
        private String patientBirthDate;
        private String patientSex;
        private String patientId;
        private boolean isStable;
        private String lastUpdate;
        private List<String> studies;
        private Map<String, String> mainDicomTags;
    }
}
