package sn.xyz.medcenter.model;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class Serie {
    private String id;                  // Orthanc ID
    private String seriesInstanceUID;    // DICOM Series Instance UID
    private String seriesNumber;         // Numéro de la série
    private String seriesDescription;    // Description de la série
    private String modality;            // Modalité (CT, MR, etc.)
    private String bodyPartExamined;     // Partie du corps examinée
    private String manufacturer;         // Fabricant de l'équipement
    private String manufacturerModelName; // Modèle de l'équipement
    
    private String studyId;             // ID Orthanc de l'étude parente
    private int numberOfInstances;      // Nombre d'instances dans la série
    private List<Instance> instances;   // Liste des instances
    
    private LocalDateTime lastUpdate;   // Dernière mise à jour dans Orthanc

    // Getters & setters
}

