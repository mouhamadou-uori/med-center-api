package sn.xyz.medcenter.model;

public class Etude {
    private String id; // Orthanc ID
    private String studyInstanceUID; // DICOM unique ID
    private String studyDate;
    private String description; // StudyDescription
    private String referringPhysicianName;

    private String patientId; // Link to patient

    // Getters & setters
}

