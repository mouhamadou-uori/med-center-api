package sn.xyz.medcenter.dto;

import sn.xyz.medcenter.model.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Classe utilitaire pour convertir les entités en DTOs
 */
public class DtoConverter {

    /**
     * Convertit une entité ProfessionnelSante en ProfessionnelSanteDTO
     * @param professionnel Entité ProfessionnelSante à convertir
     * @return ProfessionnelSanteDTO correspondant
     */
    public static ProfessionnelSanteDTO convertToProfessionnelSanteDTO(sn.xyz.medcenter.model.ProfessionnelSante professionnel) {
        if (professionnel == null) {
            return null;
        }
        return ProfessionnelSanteDTO.builder()
                .id(professionnel.getId())
                .lastName(professionnel.getLastName())
                .firstName(professionnel.getFirstName())
                .username(professionnel.getUsername())
                .email(professionnel.getEmail())
                .tel(professionnel.getTel())
                .role(professionnel.getRole())
                .dateCreation(professionnel.getDateCreation())
                .actif(professionnel.getActif())
                .specialite(professionnel.getSpecialite())
                .numeroOrdre(professionnel.getNumeroOrdre())
                .hopital(convertToHopitalShortDTO(professionnel.getHopital()))
                .build();
    }

    /**
     * Convertit une liste d'entités ProfessionnelSante en liste de ProfessionnelSanteDTO
     * @param professionnels Liste d'entités ProfessionnelSante à convertir
     * @return Liste de ProfessionnelSanteDTO correspondants
     */
    public static java.util.List<ProfessionnelSanteDTO> convertToProfessionnelSanteDTOList(java.util.List<sn.xyz.medcenter.model.ProfessionnelSante> professionnels) {
        if (professionnels == null) {
            return java.util.Collections.emptyList();
        }
        return professionnels.stream()
                .map(DtoConverter::convertToProfessionnelSanteDTO)
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Convertit une entité Hopital en HopitalShortDTO
     * @param hopital Entité Hopital à convertir
     * @return HopitalShortDTO correspondant
     */
    public static HopitalShortDTO convertToHopitalShortDTO(sn.xyz.medcenter.model.Hopital hopital) {
        if (hopital == null) {
            return null;
        }
        return HopitalShortDTO.builder()
                .id(hopital.getId())
                .nom(hopital.getNom())
                .type(hopital.getType() != null ? hopital.getType().toString() : null)
                .region(hopital.getRegion())
                .ville(hopital.getVille())
                .adresse(hopital.getAdresse())
                .telephone(hopital.getTelephone())
                .build();
    }

    /**
     * Convertit une entité Hopital en HopitalDTO complet
     * @param hopital Entité Hopital à convertir
     * @return HopitalDTO correspondant
     */
    public static HopitalDTO convertToHopitalDTO(sn.xyz.medcenter.model.Hopital hopital) {
        if (hopital == null) {
            return null;
        }
        return HopitalDTO.builder()
                .id(hopital.getId())
                .nom(hopital.getNom())
                .type(hopital.getType() != null ? hopital.getType().toString() : null)
                .region(hopital.getRegion())
                .ville(hopital.getVille())
                .adresse(hopital.getAdresse())
                .telephone(hopital.getTelephone())
                .email(hopital.getEmail())
                .siteWeb(hopital.getSiteWeb())
                .statut(hopital.getStatut())
                .professionnels(convertToProfessionnelSanteSimpleDTOList(hopital.getProfessionnels()))
                .build();
    }

    /**
     * Convertit une entité Patient en PatientDTO
     * @param patient Entité Patient à convertir
     * @return PatientDTO correspondant
     */
    public static PatientDTO convertToPatientDTO(Patient patient) {
        if (patient == null) {
            return null;
        }
        
        return PatientDTO.builder()
                .id(patient.getId())
                .lastName(patient.getLastName())
                .firstName(patient.getFirstName())
                .username(patient.getUsername())
                .email(patient.getEmail())
                .tel(patient.getTel())
                .numeroSecu(patient.getNumeroSecu())
                .adresse(patient.getAdresse())
                .contactUrgence(patient.getContactUrgence())
                .dateCreation(patient.getDateCreation())
                .actif(patient.getActif())
                .dossierMedicalId(patient.getDossierMedical() != null ? patient.getDossierMedical().getId() : null)
                .nombreConsultations(patient.getConsultations() != null ? patient.getConsultations().size() : 0)
                .build();
    }

    /**
     * Convertit une liste d'entités Patient en liste de PatientDTO
     * @param patients Liste d'entités Patient à convertir
     * @return Liste de PatientDTO correspondants
     */
    public static List<PatientDTO> convertToPatientDTOList(List<Patient> patients) {
        if (patients == null) {
            return Collections.emptyList();
        }
        
        return patients.stream()
                .map(DtoConverter::convertToPatientDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Convertit une entité DossierMedical en DossierMedicalDTO
     * @param dossier Entité DossierMedical à convertir
     * @return DossierMedicalDTO correspondant
     */
    public static DossierMedicalDTO convertToDossierMedicalDTO(DossierMedical dossier) {
        if (dossier == null) {
            return null;
        }
        
        DossierMedicalDTO.DossierMedicalDTOBuilder builder = DossierMedicalDTO.builder()
                .id(dossier.getId())
                .dateCreation(dossier.getDateCreation())
                .dateMiseAJour(dossier.getDateMiseAJour());
        
        if (dossier.getPatient() != null) {
            builder.patientId(dossier.getPatient().getId())
                   .patientNom(dossier.getPatient().getLastName())
                   .patientPrenom(dossier.getPatient().getFirstName());
        }
        
        return builder.build();
    }
    
    /**
     * Convertit une liste d'entités DossierMedical en liste de DossierMedicalDTO
     * @param dossiers Liste d'entités DossierMedical à convertir
     * @return Liste de DossierMedicalDTO correspondants
     */
    public static List<DossierMedicalDTO> convertToDossierMedicalDTOList(List<DossierMedical> dossiers) {
        if (dossiers == null) {
            return Collections.emptyList();
        }
        
        return dossiers.stream()
                .map(DtoConverter::convertToDossierMedicalDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Convertit une entité Consultation en ConsultationDTO
     * @param consultation Entité Consultation à convertir
     * @return ConsultationDTO correspondant
     */
    public static ConsultationDTO convertToConsultationDTO(Consultation consultation) {
        if (consultation == null) {
            return null;
        }
        
        ConsultationDTO.ConsultationDTOBuilder builder = ConsultationDTO.builder()
                .id(consultation.getId())
                .dateHeure(consultation.getDateHeure())
                .type(consultation.getType())
                .statut(consultation.getStatut())
                .notes(consultation.getNotes())
                .nombrePrescriptions(consultation.getPrescriptions() != null ? 
                        consultation.getPrescriptions().size() : 0);
        
        if (consultation.getProfessionnel() != null) {
            builder.professionnelId(consultation.getProfessionnel().getId())
                   .professionnelNom(consultation.getProfessionnel().getLastName())
                   .professionnelPrenom(consultation.getProfessionnel().getFirstName())
                   .professionnelSpecialite(consultation.getProfessionnel().getSpecialite());
        }
        
        if (consultation.getPatient() != null) {
            builder.patientId(consultation.getPatient().getId())
                   .patientNom(consultation.getPatient().getLastName())
                   .patientPrenom(consultation.getPatient().getFirstName());
        }
        
        return builder.build();
    }
    
    /**
     * Convertit une liste d'entités Consultation en liste de ConsultationDTO
     * @param consultations Liste d'entités Consultation à convertir
     * @return Liste de ConsultationDTO correspondants
     */
    public static List<ConsultationDTO> convertToConsultationDTOList(List<Consultation> consultations) {
        if (consultations == null) {
            return Collections.emptyList();
        }
        
        return consultations.stream()
                .map(DtoConverter::convertToConsultationDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Convertit une entité Prescription en PrescriptionDTO
     * @param prescription Entité Prescription à convertir
     * @return PrescriptionDTO correspondant
     */
    public static PrescriptionDTO convertToPrescriptionDTO(Prescription prescription) {
        if (prescription == null) {
            return null;
        }
        
        PrescriptionDTO.PrescriptionDTOBuilder builder = PrescriptionDTO.builder()
                .id(prescription.getId())
                .dateEmission(prescription.getDateEmission())
                .dateExpiration(prescription.getDateExpiration())
                .medicaments(prescription.getMedicaments())
                .instructions(prescription.getInstructions())
                .renouvelable(prescription.getRenouvelable());
        
        if (prescription.getConsultation() != null) {
            builder.consultationId(prescription.getConsultation().getId());
        }
        
        return builder.build();
    }
    
    /**
     * Convertit une liste d'entités Prescription en liste de PrescriptionDTO
     * @param prescriptions Liste d'entités Prescription à convertir
     * @return Liste de PrescriptionDTO correspondants
     */
    public static List<PrescriptionDTO> convertToPrescriptionDTOList(List<Prescription> prescriptions) {
        if (prescriptions == null) {
            return Collections.emptyList();
        }
        
        return prescriptions.stream()
                .map(DtoConverter::convertToPrescriptionDTO)
                .collect(Collectors.toList());
    }

    /**
     * Convertit une entité ProfessionnelSante en ProfessionnelSanteSimpleDTO
     * @param professionnel Entité ProfessionnelSante à convertir
     * @return ProfessionnelSanteSimpleDTO correspondant
     */
    public static ProfessionnelSanteSimpleDTO convertToProfessionnelSanteSimpleDTO(sn.xyz.medcenter.model.ProfessionnelSante professionnel) {
        if (professionnel == null) {
            return null;
        }
        return ProfessionnelSanteSimpleDTO.builder()
                .id(professionnel.getId())
                .lastName(professionnel.getLastName())
                .firstName(professionnel.getFirstName())
                .username(professionnel.getUsername())
                .email(professionnel.getEmail())
                .tel(professionnel.getTel())
                .role(professionnel.getRole())
                .dateCreation(professionnel.getDateCreation())
                .actif(professionnel.getActif())
                .specialite(professionnel.getSpecialite())
                .numeroOrdre(professionnel.getNumeroOrdre())
                .etablissement(professionnel.getEtablissement())
                .region(professionnel.getRegion())
                .build();
    }

    /**
     * Convertit une liste d'entités ProfessionnelSante en liste de ProfessionnelSanteSimpleDTO
     * @param professionnels Liste d'entités ProfessionnelSante à convertir
     * @return Liste de ProfessionnelSanteSimpleDTO correspondants
     */
    public static List<ProfessionnelSanteSimpleDTO> convertToProfessionnelSanteSimpleDTOList(List<sn.xyz.medcenter.model.ProfessionnelSante> professionnels) {
        if (professionnels == null) {
            return Collections.emptyList();
        }
        return professionnels.stream()
                .map(DtoConverter::convertToProfessionnelSanteSimpleDTO)
                .collect(Collectors.toList());
    }

    /**
     * Convertit une liste d'entités Hopital en liste de HopitalDTO
     * @param hopitaux Liste d'entités Hopital à convertir
     * @return Liste de HopitalDTO correspondants
     */
    public static List<HopitalDTO> convertToHopitalDTOList(List<sn.xyz.medcenter.model.Hopital> hopitaux) {
        if (hopitaux == null) {
            return Collections.emptyList();
        }
        return hopitaux.stream()
                .map(DtoConverter::convertToHopitalDTO)
                .collect(Collectors.toList());
    }
}
