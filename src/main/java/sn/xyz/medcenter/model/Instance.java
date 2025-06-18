package sn.xyz.medcenter.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Instance {
    private String id;                  // Orthanc ID
    private String sopInstanceUID;       // DICOM SOP Instance UID
    private String instanceNumber;       // Numéro de l'instance
    private String imageComments;        // Commentaires sur l'image
    private String imageType;            // Type d'image (ORIGINAL, DERIVED, etc.)
    private String photometricInterpretation; // Interprétation photométrique
    private Integer rows;               // Nombre de lignes
    private Integer columns;            // Nombre de colonnes
    private Integer bitsAllocated;      // Bits alloués par pixel
    private Integer bitsStored;         // Bits stockés par pixel
    private Integer highBit;            // Position du bit de poids fort
    private String pixelRepresentation; // Représentation des pixels
    
    private String seriesId;           // ID Orthanc de la série parente
    private String fileUuid;           // UUID du fichier DICOM dans Orthanc
    private Long fileSize;             // Taille du fichier en octets
    
    private LocalDateTime lastUpdate;  // Dernière mise à jour dans Orthanc
}

