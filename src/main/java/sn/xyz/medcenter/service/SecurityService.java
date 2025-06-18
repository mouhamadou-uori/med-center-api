package sn.xyz.medcenter.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.xyz.medcenter.model.Consultation;
import sn.xyz.medcenter.model.Patient;
import sn.xyz.medcenter.model.ProfessionnelSante;
import sn.xyz.medcenter.model.Utilisateur;
import sn.xyz.medcenter.repository.ConsultationRepository;
import sn.xyz.medcenter.repository.PatientRepository;
import sn.xyz.medcenter.repository.ProfessionnelSanteRepository;
import sn.xyz.medcenter.repository.UtilisateurRepository;

import java.util.List;
import java.util.Optional;

/**
 * Service pour les vérifications de sécurité personnalisées
 * Utilisé dans les expressions SpEL des annotations @PreAuthorize
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SecurityService {

    private final UtilisateurRepository utilisateurRepository;
    private final PatientRepository patientRepository;
    private final ProfessionnelSanteRepository professionnelSanteRepository;
    private final ConsultationRepository consultationRepository;

    /**
     * Vérifie si l'utilisateur courant est le patient spécifié
     * @param patientId ID du patient
     * @return true si l'utilisateur courant est le patient spécifié, false sinon
     */
    @Transactional(readOnly = true)
    public boolean isCurrentUser(Integer patientId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        
        Utilisateur utilisateur = utilisateurRepository.findByUsername(username);
        if (utilisateur == null) {
            return false;
        }
        
        Optional<Patient> patient = patientRepository.findById(patientId);
        if (patient.isEmpty()) {
            return false;
        }
        
        // Comme Patient hérite d'Utilisateur, on peut comparer directement les IDs
        return patient.get().getId().equals(utilisateur.getId());
    }

    /**
     * Vérifie si l'utilisateur courant est le professionnel de santé spécifié
     * @param professionnelId ID du professionnel de santé
     * @return true si l'utilisateur courant est le professionnel spécifié, false sinon
     */
    @Transactional(readOnly = true)
    public boolean isCurrentProfessionnel(Integer professionnelId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        
        Utilisateur utilisateur = utilisateurRepository.findByUsername(username);
        if (utilisateur == null) {
            return false;
        }
        
        Optional<ProfessionnelSante> professionnel = professionnelSanteRepository.findById(professionnelId);
        if (professionnel.isEmpty()) {
            return false;
        }
        
        // Comme ProfessionnelSante hérite d'Utilisateur, on peut comparer directement les IDs
        return professionnel.get().getId().equals(utilisateur.getId());
    }

    /**
     * Vérifie si la consultation spécifiée appartient au patient courant
     * @param consultationId ID de la consultation
     * @return true si la consultation appartient au patient courant, false sinon
     */
    @Transactional(readOnly = true)
    public boolean isPatientConsultation(Integer consultationId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        
        Utilisateur utilisateur = utilisateurRepository.findByUsername(username);
        if (utilisateur == null || !(utilisateur instanceof Patient)) {
            return false;
        }
        
        Patient patient = (Patient) utilisateur;
        
        Optional<Consultation> consultation = consultationRepository.findById(consultationId);
        if (consultation.isEmpty()) {
            return false;
        }
        
        // Vérifier si la consultation appartient au patient courant
        List<Consultation> patientConsultations = patient.getConsultations();
        return patientConsultations != null && patientConsultations.stream()
                .anyMatch(c -> c.getId().equals(consultationId));
    }
}
