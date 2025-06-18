package sn.xyz.medcenter.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sn.xyz.medcenter.model.Etude;
import sn.xyz.medcenter.model.Instance;
import sn.xyz.medcenter.model.Serie;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class OrthancMapperService {

    private static final DateTimeFormatter DICOM_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    /**
     * Convertit une réponse Orthanc en objet Etude
     */
    public Etude mapToEtude(Map<String, Object> orthancResponse) {
        Etude etude = new Etude();
        try {
            etude.setId((String) orthancResponse.get("ID"));
            etude.setStudyInstanceUID((String) orthancResponse.get("StudyInstanceUID"));
            
            String studyDate = (String) orthancResponse.get("StudyDate");
            if (studyDate != null) {
                etude.setStudyDate(LocalDateTime.parse(studyDate, DICOM_DATE_FORMATTER));
            }
            
            etude.setStudyDescription((String) orthancResponse.get("StudyDescription"));
            etude.setStudyId((String) orthancResponse.get("StudyID"));
            etude.setAccessionNumber((String) orthancResponse.get("AccessionNumber"));
            etude.setReferringPhysicianName((String) orthancResponse.get("ReferringPhysicianName"));
            
            // Informations patient
            @SuppressWarnings("unchecked")
            Map<String, Object> patientInfo = (Map<String, Object>) orthancResponse.get("PatientMainDicomTags");
            if (patientInfo != null) {
                etude.setPatientId((String) patientInfo.get("PatientID"));
                etude.setPatientName((String) patientInfo.get("PatientName"));
                etude.setPatientBirthDate((String) patientInfo.get("PatientBirthDate"));
                etude.setPatientSex((String) patientInfo.get("PatientSex"));
            }
            
            // Statistiques
            etude.setNumberOfSeries((Integer) orthancResponse.get("Series"));
            etude.setNumberOfInstances((Integer) orthancResponse.get("Instances"));
            
            // Modalités
            @SuppressWarnings("unchecked")
            List<String> modalities = (List<String>) orthancResponse.get("Modalities");
            etude.setModalities(modalities != null ? modalities : new ArrayList<>());
            
            // Date de dernière mise à jour
            String lastUpdate = (String) orthancResponse.get("LastUpdate");
            if (lastUpdate != null) {
                etude.setLastUpdate(LocalDateTime.parse(lastUpdate));
            }
        } catch (Exception e) {
            log.error("Erreur lors du mapping de l'étude: {}", e.getMessage());
        }
        return etude;
    }

    /**
     * Convertit une réponse Orthanc en objet Serie
     */
    public Serie mapToSerie(Map<String, Object> orthancResponse) {
        Serie serie = new Serie();
        try {
            serie.setId((String) orthancResponse.get("ID"));
            serie.setSeriesInstanceUID((String) orthancResponse.get("SeriesInstanceUID"));
            serie.setSeriesNumber((String) orthancResponse.get("SeriesNumber"));
            serie.setSeriesDescription((String) orthancResponse.get("SeriesDescription"));
            serie.setModality((String) orthancResponse.get("Modality"));
            serie.setBodyPartExamined((String) orthancResponse.get("BodyPartExamined"));
            serie.setManufacturer((String) orthancResponse.get("Manufacturer"));
            serie.setManufacturerModelName((String) orthancResponse.get("ManufacturerModelName"));
            
            serie.setStudyId((String) orthancResponse.get("ParentStudy"));
            serie.setNumberOfInstances((Integer) orthancResponse.get("Instances"));
            
            String lastUpdate = (String) orthancResponse.get("LastUpdate");
            if (lastUpdate != null) {
                serie.setLastUpdate(LocalDateTime.parse(lastUpdate));
            }
        } catch (Exception e) {
            log.error("Erreur lors du mapping de la série: {}", e.getMessage());
        }
        return serie;
    }

    /**
     * Convertit une réponse Orthanc en objet Instance
     */
    public Instance mapToInstance(Map<String, Object> orthancResponse) {
        Instance instance = new Instance();
        try {
            instance.setId((String) orthancResponse.get("ID"));
            instance.setSopInstanceUID((String) orthancResponse.get("SOPInstanceUID"));
            instance.setInstanceNumber((String) orthancResponse.get("InstanceNumber"));
            instance.setImageComments((String) orthancResponse.get("ImageComments"));
            instance.setImageType((String) orthancResponse.get("ImageType"));
            instance.setPhotometricInterpretation((String) orthancResponse.get("PhotometricInterpretation"));
            
            instance.setRows((Integer) orthancResponse.get("Rows"));
            instance.setColumns((Integer) orthancResponse.get("Columns"));
            instance.setBitsAllocated((Integer) orthancResponse.get("BitsAllocated"));
            instance.setBitsStored((Integer) orthancResponse.get("BitsStored"));
            instance.setHighBit((Integer) orthancResponse.get("HighBit"));
            instance.setPixelRepresentation((String) orthancResponse.get("PixelRepresentation"));
            
            instance.setSeriesId((String) orthancResponse.get("ParentSeries"));
            instance.setFileUuid((String) orthancResponse.get("FileUUID"));
            instance.setFileSize((Long) orthancResponse.get("FileSize"));
            
            String lastUpdate = (String) orthancResponse.get("LastUpdate");
            if (lastUpdate != null) {
                instance.setLastUpdate(LocalDateTime.parse(lastUpdate));
            }
        } catch (Exception e) {
            log.error("Erreur lors du mapping de l'instance: {}", e.getMessage());
        }
        return instance;
    }
        /**
     * Convertit une réponse Orthanc en objet PatientOrthanc
     */
    public sn.xyz.medcenter.model.PatientOrthanc mapToPatientOrthanc(Map<String, Object> orthancResponse) {
        sn.xyz.medcenter.model.PatientOrthanc patient = new sn.xyz.medcenter.model.PatientOrthanc();
        try {
            patient.setId((String) orthancResponse.get("ID"));
            Object isStableObj = orthancResponse.get("IsStable");
            patient.setStable(isStableObj instanceof Boolean ? (Boolean) isStableObj : false);
            @SuppressWarnings("unchecked")
            java.util.List<String> labels = (java.util.List<String>) orthancResponse.get("Labels");
            patient.setLabels(labels != null ? labels : new java.util.ArrayList<>());
            patient.setLastUpdate((String) orthancResponse.get("LastUpdate"));
            @SuppressWarnings("unchecked")
            java.util.Map<String, String> mainDicomTags = (java.util.Map<String, String>) orthancResponse.get("MainDicomTags");
            patient.setMainDicomTags(mainDicomTags != null ? mainDicomTags : new java.util.HashMap<>());
            @SuppressWarnings("unchecked")
            java.util.List<String> studies = (java.util.List<String>) orthancResponse.get("Studies");
            patient.setStudies(studies != null ? studies : new java.util.ArrayList<>());
            patient.setType((String) orthancResponse.get("Type"));
        } catch (Exception e) {
            log.error("Erreur lors du mapping du patient Orthanc: {}", e.getMessage());
        }
        return patient;
    }
}

