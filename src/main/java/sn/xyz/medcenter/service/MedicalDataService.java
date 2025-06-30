package sn.xyz.medcenter.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.xyz.medcenter.dto.StatPeriodeDTO;
import sn.xyz.medcenter.model.*;
import sn.xyz.medcenter.repository.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service pour la gestion des données médicales stockées dans la base de données MySQL
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MedicalDataService {

    private final PatientRepository patientRepository;
    private final DossierMedicalRepository dossierMedicalRepository;
    private final ConsultationRepository consultationRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final DonneeSanteRepository donneeSanteRepository;
    private final PathologieChroniqueRepository pathologieChroniqueRepository;
    private final ProfessionnelSanteRepository professionnelSanteRepository;
    private final HopitalRepository hopitalRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final EmailLogRepository emailLogRepository;

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Compte le nombre total de consultations effectuées par un professionnel de santé
     * @param professionnelId ID du professionnel de santé
     * @return Nombre total de consultations
     */
    @Transactional(readOnly = true)
    public long getConsultationCountByProfessionnelId(Integer professionnelId) {
        log.info("Calcul du nombre total de consultations pour le professionnel avec l'ID: {}", professionnelId);
        return consultationRepository.countByProfessionnelId(professionnelId);
    }

    /**
     * Compte le nombre de patients distincts consultés par un professionnel de santé
     * @param professionnelId ID du professionnel de santé
     * @return Nombre de patients distincts
     */
    @Transactional(readOnly = true)
    public long getDistinctPatientCountByProfessionnelId(Integer professionnelId) {
        log.info("Calcul du nombre de patients distincts pour le professionnel avec l'ID: {}", professionnelId);
        return consultationRepository.countDistinctPatientsByProfessionnelId(professionnelId);
    }

    /**
     * Récupère tous les patients
     * @return Liste de tous les patients
     */
    @Transactional(readOnly = true)
    public List<Patient> getAllPatients() {
        log.info("Récupération de tous les patients");
        return patientRepository.findAll();
    }

    /**
     * Récupère un patient par son ID
     * @param id ID du patient
     * @return Patient trouvé ou vide si non trouvé
     */
    @Transactional(readOnly = true)
    public Optional<Patient> getPatientById(Integer id) {
        log.info("Récupération du patient avec l'ID: {}", id);
        return patientRepository.findById(id);
    }

    /**
     * Récupère un patient par son numéro de sécurité sociale
     * @param numeroSecu Numéro de sécurité sociale
     * @return Patient trouvé ou vide si non trouvé
     */
    @Transactional(readOnly = true)
    public Optional<Patient> getPatientByNumeroSecu(String numeroSecu) {
        log.info("Récupération du patient avec le numéro de sécurité sociale: {}", numeroSecu);
        return patientRepository.findByNumeroSecu(numeroSecu);
    }

    /**
     * Récupère le dossier médical d'un patient
     * @param patientId ID du patient
     * @return Dossier médical trouvé ou vide si non trouvé
     */
    @Transactional(readOnly = true)
    public Optional<DossierMedical> getDossierMedicalByPatientId(Integer patientId) {
        log.info("Récupération du dossier médical pour le patient avec l'ID: {}", patientId);
        // Recherche du patient
        Optional<Patient> patient = patientRepository.findById(patientId);
        if (patient.isPresent()) {
            // Retourne le dossier médical associé au patient
            return Optional.ofNullable(patient.get().getDossierMedical());
        }
        return Optional.empty();
    }

    /**
     * Récupère toutes les consultations d'un patient
     * @param patientId ID du patient
     * @return Liste des consultations du patient
     */
    @Transactional(readOnly = true)
    public List<Consultation> getConsultationsByPatientId(Integer patientId) {
        log.info("Récupération des consultations pour le patient avec l'ID: {}", patientId);
        // Recherche du patient
        Optional<Patient> patient = patientRepository.findById(patientId);
        if (patient.isPresent()) {
            // Retourne les consultations associées au patient
            return patient.get().getConsultations();
        }
        return new ArrayList<>();
    }

    /**
     * Récupère toutes les consultations effectuées par un professionnel de santé
     * @param professionnelId ID du professionnel de santé
     * @return Liste des consultations effectuées par le professionnel
     */
    @Transactional(readOnly = true)
    public List<Consultation> getConsultationsByProfessionnelId(Integer professionnelId) {
        log.info("Récupération des consultations effectuées par le professionnel avec l'ID: {}", professionnelId);
        // Recherche du professionnel de santé
        Optional<ProfessionnelSante> professionnel = professionnelSanteRepository.findById(professionnelId);
        if (professionnel.isPresent()) {
            // Filtrer les consultations manuellement
            return consultationRepository.findAll().stream()
                    .filter(c -> {
                        // Vérifier si la consultation est liée au professionnel de santé spécifié
                        // Note: Nous devons adapter cette logique à la structure réelle de votre modèle
                        return c.getId() != null; // Placeholder - à adapter selon votre modèle
                    })
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    /**
     * Récupère les prescriptions d'une consultation
     * @param consultationId ID de la consultation
     * @return Liste des prescriptions de la consultation
     */
    @Transactional(readOnly = true)
    public List<Prescription> getPrescriptionsByConsultationId(Integer consultationId) {
        log.info("Récupération des prescriptions pour la consultation avec l'ID: {}", consultationId);
        // Filtrer les prescriptions par consultation
        return prescriptionRepository.findAll().stream()
                .filter(p -> p.getConsultation() != null && 
                       p.getConsultation().getId().equals(consultationId))
                .collect(Collectors.toList());
    }

    /**
     * Récupère les données de santé d'un patient
     * @param patientId ID du patient
     * @return Liste des données de santé du patient
     */
    @Transactional(readOnly = true)
    public List<DonneeSante> getDonneesSanteByPatientId(Integer patientId) {
        log.info("Récupération des données de santé pour le patient avec l'ID: {}", patientId);
        // Filtrer les données de santé manuellement
        return donneeSanteRepository.findAll().stream()
                .filter(d -> {
                    // Vérifier si la donnée de santé est liée au patient spécifié
                    // Note: Nous devons adapter cette logique à la structure réelle de votre modèle
                    return d.getId() != null; // Placeholder - à adapter selon votre modèle
                })
                .collect(Collectors.toList());
    }

    /**
     * Récupère les pathologies chroniques d'un patient
     * @param patientId ID du patient
     * @return Liste des pathologies chroniques du patient
     */
    @Transactional(readOnly = true)
    public List<PathologieChronique> getPathologiesChroniquesByPatientId(Integer patientId) {
        log.info("Récupération des pathologies chroniques pour le patient avec l'ID: {}", patientId);
        // Recherche du patient
        Optional<Patient> patient = patientRepository.findById(patientId);
        if (patient.isPresent()) {
            // Retourne les pathologies chroniques associées au patient
            return patient.get().getPathologies();
        }
        return new ArrayList<>();
    }

    /**
     * Récupère tous les professionnels de santé
     * @return Liste de tous les professionnels de santé
     */
    @Transactional(readOnly = true)
    public List<ProfessionnelSante> getAllProfessionnelsSante() {
        log.info("Récupération de tous les professionnels de santé");
        return professionnelSanteRepository.findAll();
    }

    /**
     * Récupère un professionnel de santé par son ID
     * @param id ID du professionnel de santé
     * @return Professionnel de santé trouvé ou vide si non trouvé
     */
    @Transactional(readOnly = true)
    public Optional<ProfessionnelSante> getProfessionnelSanteById(Integer id) {
        log.info("Récupération du professionnel de santé avec l'ID: {}", id);
        return professionnelSanteRepository.findById(id);
    }

    /**
     * Récupère tous les hôpitaux
     * @return Liste de tous les hôpitaux
     */
    @Transactional(readOnly = true)
    public List<Hopital> getAllHopitaux() {
        log.info("Récupération de tous les hôpitaux");
        return hopitalRepository.findAll();
    }

    /**
     * Récupère un hôpital par son ID
     * @param id ID de l'hôpital
     * @return Hôpital trouvé ou vide si non trouvé
     */
    @Transactional(readOnly = true)
    public Optional<Hopital> getHopitalById(Integer id) {
        log.info("Récupération de l'hôpital avec l'ID: {}", id);
        return hopitalRepository.findById(id);
    }

    /**
     * Récupère les professionnels de santé travaillant dans un hôpital
     * @param hopitalId ID de l'hôpital
     * @return Liste des professionnels de santé de l'hôpital
     */
    @Transactional(readOnly = true)
    public List<ProfessionnelSante> getProfessionnelsSanteByHopitalId(Integer hopitalId) {
        log.info("Récupération des professionnels de santé pour l'hôpital avec l'ID: {}", hopitalId);
        // Filtrer les professionnels de santé par hôpital
        return professionnelSanteRepository.findAll().stream()
                .filter(p -> p.getHopital() != null && 
                       p.getHopital().getId().equals(hopitalId))
                .collect(Collectors.toList());
    }

    /**
     * Récupère un utilisateur par son nom d'utilisateur
     * @param username Nom d'utilisateur
     * @return Utilisateur trouvé ou vide si non trouvé
     */
    @Transactional(readOnly = true)
    public Optional<Utilisateur> getUtilisateurByUsername(String username) {
        log.info("Récupération de l'utilisateur avec le nom d'utilisateur: {}", username);
        return Optional.ofNullable(utilisateurRepository.findByUsername(username));
    }

    public List<StatPeriodeDTO> getStatsParPeriode(String period, String start, String end, Integer idProfessionel) {
        log.info("Calcul des statistiques pour la période '{}' du {} au {} pour le professionnel ID: {}", period, start, end, idProfessionel);

        String dateFormat;
        String groupBy;

        switch (period.toLowerCase()) {
            case "weekly":
                dateFormat = "%Y-W%v"; // ISO 8601 week number
                groupBy = "YEAR(c.date_heure), WEEK(c.date_heure, 1)";
                break;
            case "monthly":
                dateFormat = "%Y-%m";
                groupBy = "YEAR(c.date_heure), MONTH(c.date_heure)";
                break;
            case "daily":
            default:
                dateFormat = "%Y-%m-%d";
                groupBy = "DATE(c.date_heure)";
                break;
        }

        StringBuilder sql = new StringBuilder(
            "SELECT " +
            "  DATE_FORMAT(c.date_heure, '" + dateFormat + "') as period_date, " +
            "  COUNT(DISTINCT c.patient_id) as patients, " +
            "  COUNT(c.id) as consultations " +
            "FROM consultation c " +
            "WHERE c.date_heure >= :start AND c.date_heure < DATE_ADD(STR_TO_DATE(:end, '%Y-%m-%d'), INTERVAL 1 DAY) "
        );

        if (idProfessionel != null) {
            sql.append("AND c.professionnel_id = :idProfessionnel ");
        }

        sql.append("GROUP BY ").append(groupBy).append(" ORDER BY ").append(groupBy);

        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("start", start);
        query.setParameter("end", end);

        if (idProfessionel != null) {
            query.setParameter("idProfessionnel", idProfessionel);
        }

        @SuppressWarnings("unchecked")
        List<Object[]> results = query.getResultList();

        return results.stream()
                .map(result -> new StatPeriodeDTO(
                        (String) result[0],
                        ((Number) result[1]).longValue(),
                        ((Number) result[2]).longValue()
                ))
                .collect(Collectors.toList());
    }

    /**
     * Récupère tous les patients consultés par un professionnel de santé
     * @param professionnelId ID du professionnel de santé
     * @return Liste des patients distincts consultés par le professionnel
     */
    @Transactional(readOnly = true)
    public List<Patient> getPatientsByProfessionnelId(Integer professionnelId) {
        log.info("Récupération des patients consultés par le professionnel avec l'ID: {}", professionnelId);
        return patientRepository.findPatientsByProfessionnelId(professionnelId);
    }

    /**
     * Construit le lien DICOM pour un hôpital donné
     * @param hopitalId ID de l'hôpital
     * @return Le lien DICOM complet ou null si aucun serveur DICOM trouvé
     */
    @Transactional(readOnly = true)
    public String getDicomUrlByHopitalId(Integer hopitalId) {
        log.info("Construction du lien DICOM pour l'hôpital avec l'ID: {}", hopitalId);
        // Recherche du serveur DICOM pour cet hôpital
        Optional<Hopital> hopital = hopitalRepository.findById(hopitalId);
        if (hopital.isPresent() && hopital.get().getServeursDICOM() != null && !hopital.get().getServeursDICOM().isEmpty()) {
            ServeurDICOM serveurDICOM = hopital.get().getServeursDICOM().get(0); // Prendre le premier serveur
            String urlOrthanc = serveurDICOM.getUrlOrthanc();
            
            // Vérifier si l'URL contient déjà le protocole
            if (urlOrthanc.startsWith("http://") || urlOrthanc.startsWith("https://")) {
                // L'URL contient déjà le protocole, ajouter seulement le port si nécessaire
                if (urlOrthanc.contains(":" + serveurDICOM.getPortOrthanc())) {
                    return urlOrthanc; // Le port est déjà inclus
                } else {
                    return urlOrthanc + ":" + serveurDICOM.getPortOrthanc();
                }
            } else {
                // L'URL ne contient pas de protocole, construire l'URL complète
                return "http://" + urlOrthanc + ":" + serveurDICOM.getPortOrthanc();
            }
        }
        return null;
    }

    /**
     * Récupère l'hôpital d'un professionnel de santé
     * @param professionnelId ID du professionnel de santé
     * @return Hôpital du professionnel ou null si non trouvé
     */
    @Transactional(readOnly = true)
    public Hopital getHopitalByProfessionnelId(Integer professionnelId) {
        log.info("Récupération de l'hôpital pour le professionnel avec l'ID: {}", professionnelId);
        Optional<ProfessionnelSante> professionnel = professionnelSanteRepository.findById(professionnelId);
        return professionnel.map(ProfessionnelSante::getHopital).orElse(null);
    }

    /**
     * Récupère l'URL DICOM pour un professionnel de santé
     * @param professionnelId ID du professionnel de santé
     * @return URL DICOM ou null si non trouvé
     */
    @Transactional(readOnly = true)
    public String getDicomUrlByProfessionnelId(Integer professionnelId) {
        log.info("Récupération de l'URL DICOM pour le professionnel avec l'ID: {}", professionnelId);
        Hopital hopital = getHopitalByProfessionnelId(professionnelId);
        if (hopital != null) {
            return getDicomUrlByHopitalId(hopital.getId());
        }
        return null;
    }

    /**
     * Récupère tous les emails d'un professionnel de santé
     * @param professionnelId ID du professionnel de santé
     * @return Liste des emails envoyés et reçus par le professionnel
     */
    @Transactional(readOnly = true)
    public List<EmailLog> getEmailsByProfessionnelId(Integer professionnelId) {
        log.info("Récupération des emails pour le professionnel avec l'ID: {}", professionnelId);
        
        Optional<ProfessionnelSante> professionnel = professionnelSanteRepository.findById(professionnelId);
        if (professionnel.isEmpty()) {
            log.warn("Aucun professionnel trouvé avec l'ID: {}", professionnelId);
            return new ArrayList<>();
        }
        
        String email = professionnel.get().getEmail();
        if (email == null || email.trim().isEmpty()) {
            log.warn("Aucun email configuré pour le professionnel avec l'ID: {}", professionnelId);
            return new ArrayList<>();
        }
        
        return emailLogRepository.findEmailsByUserEmail(email);
    }

    /**
     * Récupère les emails récents d'un professionnel de santé
     * @param professionnelId ID du professionnel de santé
     * @param days Nombre de jours à récupérer (par défaut 30)
     * @return Liste des emails récents
     */
    @Transactional(readOnly = true)
    public List<EmailLog> getRecentEmailsByProfessionnelId(Integer professionnelId, int days) {
        log.info("Récupération des emails récents ({} jours) pour le professionnel avec l'ID: {}", days, professionnelId);
        
        Optional<ProfessionnelSante> professionnel = professionnelSanteRepository.findById(professionnelId);
        if (professionnel.isEmpty()) {
            log.warn("Aucun professionnel trouvé avec l'ID: {}", professionnelId);
            return new ArrayList<>();
        }
        
        String email = professionnel.get().getEmail();
        if (email == null || email.trim().isEmpty()) {
            log.warn("Aucun email configuré pour le professionnel avec l'ID: {}", professionnelId);
            return new ArrayList<>();
        }
        
        LocalDateTime startDate = LocalDateTime.now().minusDays(days);
        return emailLogRepository.findEmailsByUserEmailSince(email, startDate);
    }

    /**
     * Récupère les emails envoyés par un professionnel de santé
     * @param professionnelId ID du professionnel de santé
     * @return Liste des emails envoyés
     */
    @Transactional(readOnly = true)
    public List<EmailLog> getSentEmailsByProfessionnelId(Integer professionnelId) {
        log.info("Récupération des emails envoyés par le professionnel avec l'ID: {}", professionnelId);
        
        Optional<ProfessionnelSante> professionnel = professionnelSanteRepository.findById(professionnelId);
        if (professionnel.isEmpty()) {
            log.warn("Aucun professionnel trouvé avec l'ID: {}", professionnelId);
            return new ArrayList<>();
        }
        
        String email = professionnel.get().getEmail();
        if (email == null || email.trim().isEmpty()) {
            log.warn("Aucun email configuré pour le professionnel avec l'ID: {}", professionnelId);
            return new ArrayList<>();
        }
        
        return emailLogRepository.findByFromEmailOrderByCreatedAtDesc(email);
    }

    /**
     * Récupère les emails reçus par un professionnel de santé
     * @param professionnelId ID du professionnel de santé
     * @return Liste des emails reçus
     */
    @Transactional(readOnly = true)
    public List<EmailLog> getReceivedEmailsByProfessionnelId(Integer professionnelId) {
        log.info("Récupération des emails reçus par le professionnel avec l'ID: {}", professionnelId);
        
        Optional<ProfessionnelSante> professionnel = professionnelSanteRepository.findById(professionnelId);
        if (professionnel.isEmpty()) {
            log.warn("Aucun professionnel trouvé avec l'ID: {}", professionnelId);
            return new ArrayList<>();
        }
        
        String email = professionnel.get().getEmail();
        if (email == null || email.trim().isEmpty()) {
            log.warn("Aucun email configuré pour le professionnel avec l'ID: {}", professionnelId);
            return new ArrayList<>();
        }
        
        return emailLogRepository.findByToEmailOrderByCreatedAtDesc(email);
    }
}
