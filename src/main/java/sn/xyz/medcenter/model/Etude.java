package sn.xyz.medcenter.model;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class Etude {
    private String id;                     // Orthanc ID
    private String studyInstanceUID;       // DICOM Study Instance UID
    private LocalDateTime studyDate;       // Date de l'étude
    private String studyDescription;       // Description de l'étude
    private String studyId;               // ID de l'étude (attribué par l'établissement)
    private String accessionNumber;        // Numéro d'accession
    private String referringPhysicianName; // Médecin référent
    
    private String patientId;             // ID Orthanc du patient
    private String patientName;           // Nom du patient
    private String patientBirthDate;      // Date de naissance du patient
    private String patientSex;            // Sexe du patient
    
    private List<String> modalities;      // Liste des modalités (CT, MR, etc.)
    private int numberOfSeries;           // Nombre de séries dans l'étude
    private int numberOfInstances;        // Nombre total d'instances
    private List<Serie> series;           // Liste des séries
    
    private LocalDateTime lastUpdate;     // Dernière mise à jour dans Orthanc
}

