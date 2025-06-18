package sn.xyz.medcenter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * DTO pour retourner les détails complets d'un patient Orthanc avec ses études et séries
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrthancPatientDetailResponseDTO {
    private String orthancUrl;
    private String patientId;
    private String patientName;
    private String patientBirthDate;
    private String patientSex;
    private String patientIdDicom;
    private boolean isStable;
    private String lastUpdate;
    private Map<String, String> patientMainDicomTags;
    private List<StudyDetailDTO> studies;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StudyDetailDTO {
        private String id;
        private String studyDate;
        private String studyTime;
        private String studyDescription;
        private String studyInstanceUID;
        private String accessionNumber;
        private boolean isStable;
        private String lastUpdate;
        private Map<String, String> studyMainDicomTags;
        private List<SeriesDetailDTO> series;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SeriesDetailDTO {
        private String id;
        private String seriesDescription;
        private String seriesNumber;
        private String modality;
        private String seriesInstanceUID;
        private String bodyPartExamined;
        private boolean isStable;
        private String lastUpdate;
        private Integer instancesCount;
        private Map<String, String> seriesMainDicomTags;
    }
}
