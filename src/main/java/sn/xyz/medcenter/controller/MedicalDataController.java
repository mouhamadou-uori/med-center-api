package sn.xyz.medcenter.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import sn.xyz.medcenter.dto.*;
import sn.xyz.medcenter.model.*;
import sn.xyz.medcenter.service.DynamicOrthancService;
import sn.xyz.medcenter.service.MedicalDataService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/medical")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
@Slf4j
public class MedicalDataController {

    private final MedicalDataService medicalDataService;
    private final DynamicOrthancService dynamicOrthancService;

    /**
     * Nombre de patients distincts consultés par un professionnel de santé
     * Accessible aux professionnels de santé et aux administrateurs
     */
    @GetMapping("/consultations/professionnel/{id}/patients-distincts")
    @PreAuthorize("hasAnyRole('PROFESSIONNEL', 'ADMIN')")
    public ResponseEntity<Long> getDistinctPatientCountByProfessionnel(@PathVariable("id") Integer professionnelId) {
        log.info("Requête: nombre de patients distincts consultés par le professionnel {}", professionnelId);
        long count = medicalDataService.getDistinctPatientCountByProfessionnelId(professionnelId);
        return ResponseEntity.ok(count);
    }

    /**
     * Nombre total de consultations effectuées par un professionnel de santé
     * Accessible aux professionnels de santé et aux administrateurs
     */
    @GetMapping("/consultations/professionnel/{id}/total")
    @PreAuthorize("hasAnyRole('PROFESSIONNEL', 'ADMIN')")
    public ResponseEntity<Long> getConsultationCountByProfessionnel(@PathVariable("id") Integer professionnelId) {
        log.info("Requête: nombre total de consultations pour le professionnel {}", professionnelId);
        long count = medicalDataService.getConsultationCountByProfessionnelId(professionnelId);
        return ResponseEntity.ok(count);
    }

    /**
     * Récupère tous les patients
     * Accessible aux professionnels de santé et aux administrateurs
     */
    @GetMapping("/patients")
    @PreAuthorize("hasAnyRole('PROFESSIONNEL', 'RADIOLOGUE', 'ADMIN')")
    public ResponseEntity<List<PatientDTO>> getAllPatients() {
        log.info("Requête: récupérer tous les patients");
        List<Patient> patients = medicalDataService.getAllPatients();
        List<PatientDTO> patientDTOs = DtoConverter.convertToPatientDTOList(patients);
        return ResponseEntity.ok(patientDTOs);
    }

    /**
     * Récupère un patient par son ID
     * Accessible aux professionnels de santé et aux administrateurs
     */
    @GetMapping("/patients/{id}")
    @PreAuthorize("hasAnyRole('PROFESSIONNEL', 'RADIOLOGUE', 'ADMIN')")
    public ResponseEntity<PatientDTO> getPatientById(@PathVariable Integer id) {
        log.info("Requête: récupérer le patient avec l'ID: {}", id);
        return medicalDataService.getPatientById(id)
                .map(DtoConverter::convertToPatientDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Récupère le dossier médical d'un patient
     * Accessible aux professionnels de santé et au patient concerné
     */
    @GetMapping("/patients/{patientId}/dossier")
    @PreAuthorize("hasAnyRole('PROFESSIONNEL', 'RADIOLOGUE') or (hasRole('PATIENT') and @securityService.isCurrentUser(#patientId))")
    public ResponseEntity<DossierMedicalDTO> getDossierMedicalByPatientId(@PathVariable Integer patientId) {
        log.info("Requête: récupérer le dossier médical du patient avec l'ID: {}", patientId);
        return medicalDataService.getDossierMedicalByPatientId(patientId)
                .map(DtoConverter::convertToDossierMedicalDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Récupère les consultations d'un patient
     * Accessible aux professionnels de santé et au patient concerné
     */
    @GetMapping("/patients/{patientId}/consultations")
    @PreAuthorize("hasAnyRole('PROFESSIONNEL', 'RADIOLOGUE') or (hasRole('PATIENT') and @securityService.isCurrentUser(#patientId))")
    public ResponseEntity<List<ConsultationDTO>> getConsultationsByPatientId(@PathVariable Integer patientId) {
        log.info("Requête: récupérer les consultations du patient avec l'ID: {}", patientId);
        List<Consultation> consultations = medicalDataService.getConsultationsByPatientId(patientId);
        List<ConsultationDTO> consultationDTOs = DtoConverter.convertToConsultationDTOList(consultations);
        return ResponseEntity.ok(consultationDTOs);
    }

    /**
     * Récupère les prescriptions d'une consultation
     * Accessible aux professionnels de santé et au patient concerné
     */
    @GetMapping("/consultations/{consultationId}/prescriptions")
    @PreAuthorize("hasAnyRole('PROFESSIONNEL', 'RADIOLOGUE') or (hasRole('PATIENT') and @securityService.isPatientConsultation(#consultationId))")
    public ResponseEntity<List<PrescriptionDTO>> getPrescriptionsByConsultationId(@PathVariable Integer consultationId) {
        log.info("Requête: récupérer les prescriptions de la consultation avec l'ID: {}", consultationId);
        List<Prescription> prescriptions = medicalDataService.getPrescriptionsByConsultationId(consultationId);
        List<PrescriptionDTO> prescriptionDTOs = DtoConverter.convertToPrescriptionDTOList(prescriptions);
        return ResponseEntity.ok(prescriptionDTOs);
    }

    /**
     * Récupère les données de santé d'un patient
     * Accessible aux professionnels de santé et au patient concerné
     */
    @GetMapping("/patients/{patientId}/donnees-sante")
    @PreAuthorize("hasAnyRole('PROFESSIONNEL', 'RADIOLOGUE') or (hasRole('PATIENT') and @securityService.isCurrentUser(#patientId))")
    public ResponseEntity<List<DonneeSante>> getDonneesSanteByPatientId(@PathVariable Integer patientId) {
        log.info("Requête: récupérer les données de santé du patient avec l'ID: {}", patientId);
        List<DonneeSante> donneesSante = medicalDataService.getDonneesSanteByPatientId(patientId);
        return ResponseEntity.ok(donneesSante);
    }

    /**
     * Récupère les pathologies chroniques d'un patient
     * Accessible aux professionnels de santé et au patient concerné
     */
    @GetMapping("/patients/{patientId}/pathologies")
    @PreAuthorize("hasAnyRole('PROFESSIONNEL', 'RADIOLOGUE') or (hasRole('PATIENT') and @securityService.isCurrentUser(#patientId))")
    public ResponseEntity<List<PathologieChronique>> getPathologiesChroniquesByPatientId(@PathVariable Integer patientId) {
        log.info("Requête: récupérer les pathologies chroniques du patient avec l'ID: {}", patientId);
        List<PathologieChronique> pathologies = medicalDataService.getPathologiesChroniquesByPatientId(patientId);
        return ResponseEntity.ok(pathologies);
    }

    /**
     * Récupère tous les professionnels de santé
     * Accessible à tous les utilisateurs authentifiés
     */
    @GetMapping("/professionnels")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ProfessionnelSanteDTO>> getAllProfessionnelsSante() {
        log.info("Requête: récupérer tous les professionnels de santé");
        List<ProfessionnelSanteDTO> dtos = sn.xyz.medcenter.dto.DtoConverter.convertToProfessionnelSanteDTOList(medicalDataService.getAllProfessionnelsSante());
        return ResponseEntity.ok(dtos);
    }

    /**
     * Récupère un professionnel de santé par son ID
     * Accessible à tous les utilisateurs authentifiés
     */
    @GetMapping("/professionnels/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ProfessionnelSanteDTO> getProfessionnelSanteById(@PathVariable Integer id) {
        log.info("Requête: récupérer le professionnel de santé avec l'ID: {}", id);
        return medicalDataService.getProfessionnelSanteById(id)
                .map(sn.xyz.medcenter.dto.DtoConverter::convertToProfessionnelSanteDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Récupère les consultations effectuées par un professionnel de santé
     * Accessible au professionnel concerné et aux administrateurs
     */
    @GetMapping("/professionnels/{professionnelId}/consultations")
    @PreAuthorize("hasRole('ADMIN') or (hasAnyRole('PROFESSIONNEL', 'RADIOLOGUE') and @securityService.isCurrentProfessionnel(#professionnelId))")
    public ResponseEntity<List<ConsultationDTO>> getConsultationsByProfessionnelId(@PathVariable Integer professionnelId) {
        log.info("Requête: récupérer les consultations du professionnel avec l'ID: {}", professionnelId);
        List<Consultation> consultations = medicalDataService.getConsultationsByProfessionnelId(professionnelId);
        List<ConsultationDTO> consultationDTOs = DtoConverter.convertToConsultationDTOList(consultations);
        return ResponseEntity.ok(consultationDTOs);
    }

    /**
     * Récupère tous les hôpitaux
     * Accessible à tous les utilisateurs authentifiés
     */
    @GetMapping("/hopitaux")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<HopitalDTO>> getAllHopitaux() {
        log.info("Requête: récupérer tous les hôpitaux");
        List<Hopital> hopitaux = medicalDataService.getAllHopitaux();
        List<HopitalDTO> hopitalDTOs = DtoConverter.convertToHopitalDTOList(hopitaux);
        return ResponseEntity.ok(hopitalDTOs);
    }

    /**
     * Récupère un hôpital par son ID
     * Accessible à tous les utilisateurs authentifiés
     */
    @GetMapping("/hopitaux/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<HopitalDTO> getHopitalById(@PathVariable Integer id) {
        log.info("Requête: récupérer l' hôpital avec l'ID: {}", id);
        return medicalDataService.getHopitalById(id)
                .map(DtoConverter::convertToHopitalDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Récupère les professionnels de santé travaillant dans un hôpital
     * Accessible à tous les utilisateurs authentifiés
     */
    @GetMapping("/hopitaux/{hopitalId}/professionnels")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ProfessionnelSanteSimpleDTO>> getProfessionnelsSanteByHopitalId(@PathVariable Integer hopitalId) {
        log.info("Requête: récupérer les professionnels de santé de l'hôpital avec l'ID: {}", hopitalId);
        List<ProfessionnelSante> professionnels = medicalDataService.getProfessionnelsSanteByHopitalId(hopitalId);
        List<ProfessionnelSanteSimpleDTO> professionnelDTOs = DtoConverter.convertToProfessionnelSanteSimpleDTOList(professionnels);
        return ResponseEntity.ok(professionnelDTOs);
    }

    /**
     * Récupère un utilisateur par son username
     * Accessible à tous les utilisateurs authentifiés
     */
    @GetMapping("/utilisateur/{username}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getUtilisateurByUsername(@PathVariable String username) {
        log.info("Requête: récupérer l'utilisateur avec le username: {}", username);
        return medicalDataService.getUtilisateurByUsername(username)
                .map(utilisateur -> {
                    // Conversion en DTO approprié selon le type d'utilisateur
                    if (utilisateur instanceof ProfessionnelSante) {
                        ProfessionnelSanteDTO dto = DtoConverter.convertToProfessionnelSanteDTO((ProfessionnelSante) utilisateur);
                        return ResponseEntity.ok(dto);
                    } else if (utilisateur instanceof Patient) {
                        PatientDTO dto = DtoConverter.convertToPatientDTO((Patient) utilisateur);
                        return ResponseEntity.ok(dto);
                    } else {
                        // Pour les autres types d'utilisateurs (Administrateur, etc.)
                        // Retourner un DTO basique sans références circulaires
                        return ResponseEntity.ok(utilisateur);
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Statistiques patients/consultations par période (jour/semaine/mois)
     * @param period "daily" | "weekly" | "monthly"
     * @param start date de début (format ISO)
     * @param end date de fin (format ISO)
     * @param idProfessionel id du professionnel (optionnel)
     */
    @GetMapping("/stats/period/{period}/{start}/{end}/{idProfessionel}")
    @PreAuthorize("hasAnyRole('PROFESSIONNEL', 'ADMIN')")
    public ResponseEntity<List<StatPeriodeDTO>> getStatsParPeriode(
            @PathVariable String period,
            @PathVariable String start,
            @PathVariable String end,
            @PathVariable Integer idProfessionel) {
        log.info("Requête: statistiques par période {} du {} au {}", period, start, end);
        List<StatPeriodeDTO> stats = medicalDataService.getStatsParPeriode(period, start, end, idProfessionel);
        return ResponseEntity.ok(stats);
    }

    /**
     * Récupère tous les patients consultés par un professionnel de santé spécifique
     * Accessible aux professionnels de santé et aux administrateurs
     */
    @GetMapping("/professionnels/{idProfessionnel}/patients")
    @PreAuthorize("hasAnyRole('PROFESSIONNEL', 'ADMIN')")
    public ResponseEntity<List<PatientDTO>> getPatientsByProfessionnel(@PathVariable Integer idProfessionnel) {
        log.info("Requête: récupération des patients consultés par le professionnel avec l'ID: {}", idProfessionnel);
        
        List<Patient> patients = medicalDataService.getPatientsByProfessionnelId(idProfessionnel);
        List<PatientDTO> patientDTOs = DtoConverter.convertToPatientDTOList(patients);
        
        log.info("Nombre de patients trouvés pour le professionnel {}: {}", idProfessionnel, patientDTOs.size());
        return ResponseEntity.ok(patientDTOs);
    }

    /**
     * Récupère le lien DICOM pour un hôpital donné
     * Accessible à tous les utilisateurs authentifiés
     */
    @GetMapping("/hopitaux/{hopitalId}/dicom-url")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> getDicomUrlByHopitalId(@PathVariable Integer hopitalId) {
        log.info("Requête: récupération du lien DICOM pour l'hôpital avec l'ID: {}", hopitalId);
        
        String dicomUrl = medicalDataService.getDicomUrlByHopitalId(hopitalId);
        
        if (dicomUrl != null) {
            log.info("Lien DICOM trouvé pour l'hôpital {}: {}", hopitalId, dicomUrl);
            return ResponseEntity.ok(dicomUrl);
        } else {
            log.warn("Aucun serveur DICOM trouvé pour l'hôpital avec l'ID: {}", hopitalId);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Récupère tous les patients du serveur Orthanc d'un hôpital spécifique
     * Accessible aux professionnels de santé et aux administrateurs
     */
    @GetMapping("/hopitaux/{hopitalId}/patients-orthanc")
    @PreAuthorize("hasAnyRole('PROFESSIONNEL', 'ADMIN')")
    public Mono<ResponseEntity<OrthancPatientsResponseDTO>> getOrthancPatientsByHopital(@PathVariable Integer hopitalId) {
        log.info("Requête: récupération des patients Orthanc pour l'hôpital avec l'ID: {}", hopitalId);
        
        // Récupérer l'hôpital et l'URL DICOM
        Optional<Hopital> hopitalOpt = medicalDataService.getHopitalById(hopitalId);
        if (hopitalOpt.isEmpty()) {
            log.warn("Aucun hôpital trouvé avec l'ID: {}", hopitalId);
            return Mono.just(ResponseEntity.notFound().build());
        }
        
        Hopital hopital = hopitalOpt.get();
        String orthancUrl = medicalDataService.getDicomUrlByHopitalId(hopitalId);
        if (orthancUrl == null) {
            log.warn("Aucun serveur DICOM trouvé pour l'hôpital avec l'ID: {}", hopitalId);
            return Mono.just(ResponseEntity.notFound().build());
        }
        
        // Récupérer tous les patients depuis Orthanc
        return dynamicOrthancService.getPatients(orthancUrl)
                .flatMap(patientIds -> {
                    if (patientIds == null || patientIds.length == 0) {
                        log.info("Aucun patient trouvé sur le serveur Orthanc: {}", orthancUrl);
                        return Mono.just(OrthancPatientsResponseDTO.builder()
                                .orthancUrl(orthancUrl)
                                .hopitalId(hopital.getId())
                                .hopitalNom(hopital.getNom())
                                .patients(new ArrayList<>())
                                .build());
                    }
                    
                    // Pour chaque patient, récupérer ses détails
                    List<Mono<OrthancPatientsResponseDTO.OrthancPatientDetailDTO>> patientDetails = 
                        Arrays.stream(patientIds)
                            .map(patientId -> dynamicOrthancService.getPatient(orthancUrl, patientId)
                                .map(patientData -> {
                                    @SuppressWarnings("unchecked")
                                    Map<String, String> mainDicomTags = (Map<String, String>) patientData.get("MainDicomTags");
                                    @SuppressWarnings("unchecked")
                                    List<String> studies = (List<String>) patientData.get("Studies");
                                    
                                    return OrthancPatientsResponseDTO.OrthancPatientDetailDTO.builder()
                                            .id((String) patientData.get("ID"))
                                            .patientName(mainDicomTags != null ? mainDicomTags.get("PatientName") : null)
                                            .patientBirthDate(mainDicomTags != null ? mainDicomTags.get("PatientBirthDate") : null)
                                            .patientSex(mainDicomTags != null ? mainDicomTags.get("PatientSex") : null)
                                            .patientId(mainDicomTags != null ? mainDicomTags.get("PatientID") : null)
                                            .isStable((Boolean) patientData.getOrDefault("IsStable", false))
                                            .lastUpdate((String) patientData.get("LastUpdate"))
                                            .studies(studies != null ? studies : new ArrayList<>())
                                            .mainDicomTags(mainDicomTags)
                                            .build();
                                })
                                .onErrorReturn(OrthancPatientsResponseDTO.OrthancPatientDetailDTO.builder()
                                        .id(patientId)
                                        .build())
                            )
                            .collect(Collectors.toList());
                    
                    return Mono.zip(patientDetails, objects -> Arrays.stream(objects)
                            .map(obj -> (OrthancPatientsResponseDTO.OrthancPatientDetailDTO) obj)
                            .collect(Collectors.toList()))
                        .map(patientDetailsList -> OrthancPatientsResponseDTO.builder()
                                .orthancUrl(orthancUrl)
                                .hopitalId(hopital.getId())
                                .hopitalNom(hopital.getNom())
                                .patients(patientDetailsList)
                                .build());
                })
                .map(ResponseEntity::ok)
                .onErrorReturn(ResponseEntity.status(500).build());
    }

    /**
     * Récupère les détails complets d'un patient Orthanc avec ses études et séries
     * Accessible aux professionnels de santé et aux administrateurs
     */
    @GetMapping("/hopitaux/{hopitalId}/patients-orthanc/{patientId}/details")
    @PreAuthorize("hasAnyRole('PROFESSIONNEL', 'ADMIN')")
    public Mono<ResponseEntity<OrthancPatientDetailResponseDTO>> getOrthancPatientDetails(
            @PathVariable Integer hopitalId, 
            @PathVariable String patientId) {
        log.info("Requête: récupération des détails du patient Orthanc {} pour l'hôpital avec l'ID: {}", patientId, hopitalId);
        
        // Récupérer l'URL DICOM de l'hôpital
        String orthancUrl = medicalDataService.getDicomUrlByHopitalId(hopitalId);
        if (orthancUrl == null) {
            log.warn("Aucun serveur DICOM trouvé pour l'hôpital avec l'ID: {}", hopitalId);
            return Mono.just(ResponseEntity.notFound().build());
        }
        
        // Récupérer les détails du patient
        return dynamicOrthancService.getPatient(orthancUrl, patientId)
                .flatMap(patientData -> {
                    @SuppressWarnings("unchecked")
                    Map<String, String> patientMainDicomTags = (Map<String, String>) patientData.get("MainDicomTags");
                    @SuppressWarnings("unchecked")
                    List<String> studyIds = (List<String>) patientData.get("Studies");
                    
                    if (studyIds == null || studyIds.isEmpty()) {
                        // Patient sans études
                        OrthancPatientDetailResponseDTO response = OrthancPatientDetailResponseDTO.builder()
                                .orthancUrl(orthancUrl)
                                .patientId((String) patientData.get("ID"))
                                .patientName(patientMainDicomTags != null ? patientMainDicomTags.get("PatientName") : null)
                                .patientBirthDate(patientMainDicomTags != null ? patientMainDicomTags.get("PatientBirthDate") : null)
                                .patientSex(patientMainDicomTags != null ? patientMainDicomTags.get("PatientSex") : null)
                                .patientIdDicom(patientMainDicomTags != null ? patientMainDicomTags.get("PatientID") : null)
                                .isStable((Boolean) patientData.getOrDefault("IsStable", false))
                                .lastUpdate((String) patientData.get("LastUpdate"))
                                .patientMainDicomTags(patientMainDicomTags)
                                .studies(new ArrayList<>())
                                .build();
                        return Mono.just(response);
                    }
                    
                    // Récupérer les détails de chaque étude
                    List<Mono<OrthancPatientDetailResponseDTO.StudyDetailDTO>> studyDetailMonos = 
                        studyIds.stream()
                            .map(studyId -> dynamicOrthancService.getStudy(orthancUrl, studyId)
                                .flatMap(studyData -> {
                                    @SuppressWarnings("unchecked")
                                    Map<String, String> studyMainDicomTags = (Map<String, String>) studyData.get("MainDicomTags");
                                    @SuppressWarnings("unchecked")
                                    List<String> seriesIds = (List<String>) studyData.get("Series");
                                    
                                    if (seriesIds == null || seriesIds.isEmpty()) {
                                        // Étude sans séries
                                        return Mono.just(OrthancPatientDetailResponseDTO.StudyDetailDTO.builder()
                                                .id((String) studyData.get("ID"))
                                                .studyDate(studyMainDicomTags != null ? studyMainDicomTags.get("StudyDate") : null)
                                                .studyTime(studyMainDicomTags != null ? studyMainDicomTags.get("StudyTime") : null)
                                                .studyDescription(studyMainDicomTags != null ? studyMainDicomTags.get("StudyDescription") : null)
                                                .studyInstanceUID(studyMainDicomTags != null ? studyMainDicomTags.get("StudyInstanceUID") : null)
                                                .accessionNumber(studyMainDicomTags != null ? studyMainDicomTags.get("AccessionNumber") : null)
                                                .isStable((Boolean) studyData.getOrDefault("IsStable", false))
                                                .lastUpdate((String) studyData.get("LastUpdate"))
                                                .studyMainDicomTags(studyMainDicomTags)
                                                .series(new ArrayList<>())
                                                .build());
                                    }
                                    
                                    // Récupérer les détails de chaque série
                                    List<Mono<OrthancPatientDetailResponseDTO.SeriesDetailDTO>> seriesDetailMonos = 
                                        seriesIds.stream()
                                            .map(seriesId -> dynamicOrthancService.getSeries(orthancUrl, seriesId)
                                                .map(seriesData -> {
                                                    @SuppressWarnings("unchecked")
                                                    Map<String, String> seriesMainDicomTags = (Map<String, String>) seriesData.get("MainDicomTags");
                                                    @SuppressWarnings("unchecked")
                                                    List<String> instances = (List<String>) seriesData.get("Instances");
                                                    
                                                    return OrthancPatientDetailResponseDTO.SeriesDetailDTO.builder()
                                                            .id((String) seriesData.get("ID"))
                                                            .seriesDescription(seriesMainDicomTags != null ? seriesMainDicomTags.get("SeriesDescription") : null)
                                                            .seriesNumber(seriesMainDicomTags != null ? seriesMainDicomTags.get("SeriesNumber") : null)
                                                            .modality(seriesMainDicomTags != null ? seriesMainDicomTags.get("Modality") : null)
                                                            .seriesInstanceUID(seriesMainDicomTags != null ? seriesMainDicomTags.get("SeriesInstanceUID") : null)
                                                            .bodyPartExamined(seriesMainDicomTags != null ? seriesMainDicomTags.get("BodyPartExamined") : null)
                                                            .isStable((Boolean) seriesData.getOrDefault("IsStable", false))
                                                            .lastUpdate((String) seriesData.get("LastUpdate"))
                                                            .instancesCount(instances != null ? instances.size() : 0)
                                                            .seriesMainDicomTags(seriesMainDicomTags)
                                                            .build();
                                                })
                                                .onErrorReturn(OrthancPatientDetailResponseDTO.SeriesDetailDTO.builder()
                                                        .id(seriesId)
                                                        .build())
                                            )
                                            .collect(Collectors.toList());
                                    
                                    return Mono.zip(seriesDetailMonos, objects -> Arrays.stream(objects)
                                            .map(obj -> (OrthancPatientDetailResponseDTO.SeriesDetailDTO) obj)
                                            .collect(Collectors.toList()))
                                        .map(seriesDetailsList -> OrthancPatientDetailResponseDTO.StudyDetailDTO.builder()
                                                .id((String) studyData.get("ID"))
                                                .studyDate(studyMainDicomTags != null ? studyMainDicomTags.get("StudyDate") : null)
                                                .studyTime(studyMainDicomTags != null ? studyMainDicomTags.get("StudyTime") : null)
                                                .studyDescription(studyMainDicomTags != null ? studyMainDicomTags.get("StudyDescription") : null)
                                                .studyInstanceUID(studyMainDicomTags != null ? studyMainDicomTags.get("StudyInstanceUID") : null)
                                                .accessionNumber(studyMainDicomTags != null ? studyMainDicomTags.get("AccessionNumber") : null)
                                                .isStable((Boolean) studyData.getOrDefault("IsStable", false))
                                                .lastUpdate((String) studyData.get("LastUpdate"))
                                                .studyMainDicomTags(studyMainDicomTags)
                                                .series(seriesDetailsList)
                                                .build());
                                })
                                .onErrorReturn(OrthancPatientDetailResponseDTO.StudyDetailDTO.builder()
                                        .id(studyId)
                                        .series(new ArrayList<>())
                                        .build())
                            )
                            .collect(Collectors.toList());
                    
                    return Mono.zip(studyDetailMonos, objects -> Arrays.stream(objects)
                            .map(obj -> (OrthancPatientDetailResponseDTO.StudyDetailDTO) obj)
                            .collect(Collectors.toList()))
                        .map(studyDetailsList -> OrthancPatientDetailResponseDTO.builder()
                                .orthancUrl(orthancUrl)
                                .patientId((String) patientData.get("ID"))
                                .patientName(patientMainDicomTags != null ? patientMainDicomTags.get("PatientName") : null)
                                .patientBirthDate(patientMainDicomTags != null ? patientMainDicomTags.get("PatientBirthDate") : null)
                                .patientSex(patientMainDicomTags != null ? patientMainDicomTags.get("PatientSex") : null)
                                .patientIdDicom(patientMainDicomTags != null ? patientMainDicomTags.get("PatientID") : null)
                                .isStable((Boolean) patientData.getOrDefault("IsStable", false))
                                .lastUpdate((String) patientData.get("LastUpdate"))
                                .patientMainDicomTags(patientMainDicomTags)
                                .studies(studyDetailsList)
                                .build());
                })
                .map(ResponseEntity::ok)
                .onErrorReturn(ResponseEntity.status(500).build());
    }

    /**
     * Récupère les détails complets d'un patient Orthanc avec ses études et séries
     * en cherchant dans tous les hôpitaux (version alternative)
     * Accessible aux professionnels de santé et aux administrateurs
     */
    @GetMapping("/patients-orthanc/{patientId}/details")
    @PreAuthorize("hasAnyRole('PROFESSIONNEL', 'ADMIN')")
    public Mono<ResponseEntity<OrthancPatientDetailResponseDTO>> getOrthancPatientDetailsFromAllHospitals(@PathVariable String patientId) {
        log.info("Requête: récupération des détails du patient Orthanc {} en cherchant dans tous les hôpitaux", patientId);
        
        // Récupérer tous les hôpitaux
        List<Hopital> hopitaux = medicalDataService.getAllHopitaux();
        
        // Chercher le patient dans chaque hôpital
        List<Mono<OrthancPatientDetailResponseDTO>> searchMonos = hopitaux.stream()
                .map(hopital -> {
                    String orthancUrl = medicalDataService.getDicomUrlByHopitalId(hopital.getId());
                    if (orthancUrl == null) {
                        return Mono.<OrthancPatientDetailResponseDTO>empty();
                    }
                    
                    return dynamicOrthancService.getPatient(orthancUrl, patientId)
                            .flatMap(patientData -> buildPatientDetailResponse(orthancUrl, patientData))
                            .onErrorResume(e -> {
                                log.debug("Patient {} non trouvé dans l'hôpital {} ({})", patientId, hopital.getNom(), orthancUrl);
                                return Mono.empty();
                            });
                })
                .collect(Collectors.toList());
        
        return Mono.firstWithValue(searchMonos)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()))
                .onErrorReturn(ResponseEntity.status(500).build());
    }

    /**
     * Méthode utilitaire pour construire la réponse complète des détails d'un patient
     */
    private Mono<OrthancPatientDetailResponseDTO> buildPatientDetailResponse(String orthancUrl, Map<String, Object> patientData) {
        @SuppressWarnings("unchecked")
        Map<String, String> patientMainDicomTags = (Map<String, String>) patientData.get("MainDicomTags");
        @SuppressWarnings("unchecked")
        List<String> studyIds = (List<String>) patientData.get("Studies");
        
        if (studyIds == null || studyIds.isEmpty()) {
            // Patient sans études
            OrthancPatientDetailResponseDTO response = OrthancPatientDetailResponseDTO.builder()
                    .orthancUrl(orthancUrl)
                    .patientId((String) patientData.get("ID"))
                    .patientName(patientMainDicomTags != null ? patientMainDicomTags.get("PatientName") : null)
                    .patientBirthDate(patientMainDicomTags != null ? patientMainDicomTags.get("PatientBirthDate") : null)
                    .patientSex(patientMainDicomTags != null ? patientMainDicomTags.get("PatientSex") : null)
                    .patientIdDicom(patientMainDicomTags != null ? patientMainDicomTags.get("PatientID") : null)
                    .isStable((Boolean) patientData.getOrDefault("IsStable", false))
                    .lastUpdate((String) patientData.get("LastUpdate"))
                    .patientMainDicomTags(patientMainDicomTags)
                    .studies(new ArrayList<>())
                    .build();
            return Mono.just(response);
        }
        
        // Récupérer les détails de chaque étude avec leurs séries
        List<Mono<OrthancPatientDetailResponseDTO.StudyDetailDTO>> studyDetailMonos = 
            studyIds.stream()
                .map(studyId -> buildStudyDetailWithSeries(orthancUrl, studyId))
                .collect(Collectors.toList());
        
        return Mono.zip(studyDetailMonos, objects -> Arrays.stream(objects)
                .map(obj -> (OrthancPatientDetailResponseDTO.StudyDetailDTO) obj)
                .collect(Collectors.toList()))
            .map(studyDetailsList -> OrthancPatientDetailResponseDTO.builder()
                    .orthancUrl(orthancUrl)
                    .patientId((String) patientData.get("ID"))
                    .patientName(patientMainDicomTags != null ? patientMainDicomTags.get("PatientName") : null)
                    .patientBirthDate(patientMainDicomTags != null ? patientMainDicomTags.get("PatientBirthDate") : null)
                    .patientSex(patientMainDicomTags != null ? patientMainDicomTags.get("PatientSex") : null)
                    .patientIdDicom(patientMainDicomTags != null ? patientMainDicomTags.get("PatientID") : null)
                    .isStable((Boolean) patientData.getOrDefault("IsStable", false))
                    .lastUpdate((String) patientData.get("LastUpdate"))
                    .patientMainDicomTags(patientMainDicomTags)
                    .studies(studyDetailsList)
                    .build());
    }

    /**
     * Méthode utilitaire pour construire les détails d'une étude avec ses séries
     */
    private Mono<OrthancPatientDetailResponseDTO.StudyDetailDTO> buildStudyDetailWithSeries(String orthancUrl, String studyId) {
        return dynamicOrthancService.getStudy(orthancUrl, studyId)
                .flatMap(studyData -> {
                    @SuppressWarnings("unchecked")
                    Map<String, String> studyMainDicomTags = (Map<String, String>) studyData.get("MainDicomTags");
                    @SuppressWarnings("unchecked")
                    List<String> seriesIds = (List<String>) studyData.get("Series");
                    
                    if (seriesIds == null || seriesIds.isEmpty()) {
                        // Étude sans séries
                        return Mono.just(OrthancPatientDetailResponseDTO.StudyDetailDTO.builder()
                                .id((String) studyData.get("ID"))
                                .studyDate(studyMainDicomTags != null ? studyMainDicomTags.get("StudyDate") : null)
                                .studyTime(studyMainDicomTags != null ? studyMainDicomTags.get("StudyTime") : null)
                                .studyDescription(studyMainDicomTags != null ? studyMainDicomTags.get("StudyDescription") : null)
                                .studyInstanceUID(studyMainDicomTags != null ? studyMainDicomTags.get("StudyInstanceUID") : null)
                                .accessionNumber(studyMainDicomTags != null ? studyMainDicomTags.get("AccessionNumber") : null)
                                .isStable((Boolean) studyData.getOrDefault("IsStable", false))
                                .lastUpdate((String) studyData.get("LastUpdate"))
                                .studyMainDicomTags(studyMainDicomTags)
                                .series(new ArrayList<>())
                                .build());
                    }
                    
                    // Récupérer les détails de chaque série
                    List<Mono<OrthancPatientDetailResponseDTO.SeriesDetailDTO>> seriesDetailMonos = 
                        seriesIds.stream()
                            .map(seriesId -> dynamicOrthancService.getSeries(orthancUrl, seriesId)
                                .map(seriesData -> {
                                    @SuppressWarnings("unchecked")
                                    Map<String, String> seriesMainDicomTags = (Map<String, String>) seriesData.get("MainDicomTags");
                                    @SuppressWarnings("unchecked")
                                    List<String> instances = (List<String>) seriesData.get("Instances");
                                    
                                    return OrthancPatientDetailResponseDTO.SeriesDetailDTO.builder()
                                            .id((String) seriesData.get("ID"))
                                            .seriesDescription(seriesMainDicomTags != null ? seriesMainDicomTags.get("SeriesDescription") : null)
                                            .seriesNumber(seriesMainDicomTags != null ? seriesMainDicomTags.get("SeriesNumber") : null)
                                            .modality(seriesMainDicomTags != null ? seriesMainDicomTags.get("Modality") : null)
                                            .seriesInstanceUID(seriesMainDicomTags != null ? seriesMainDicomTags.get("SeriesInstanceUID") : null)
                                            .bodyPartExamined(seriesMainDicomTags != null ? seriesMainDicomTags.get("BodyPartExamined") : null)
                                            .isStable((Boolean) seriesData.getOrDefault("IsStable", false))
                                            .lastUpdate((String) seriesData.get("LastUpdate"))
                                            .instancesCount(instances != null ? instances.size() : 0)
                                            .seriesMainDicomTags(seriesMainDicomTags)
                                            .build();
                                })
                                .onErrorReturn(OrthancPatientDetailResponseDTO.SeriesDetailDTO.builder()
                                        .id(seriesId)
                                        .build())
                            )
                            .collect(Collectors.toList());
                    
                    return Mono.zip(seriesDetailMonos, objects -> Arrays.stream(objects)
                            .map(obj -> (OrthancPatientDetailResponseDTO.SeriesDetailDTO) obj)
                            .collect(Collectors.toList()))
                        .map(seriesDetailsList -> OrthancPatientDetailResponseDTO.StudyDetailDTO.builder()
                                .id((String) studyData.get("ID"))
                                .studyDate(studyMainDicomTags != null ? studyMainDicomTags.get("StudyDate") : null)
                                .studyTime(studyMainDicomTags != null ? studyMainDicomTags.get("StudyTime") : null)
                                .studyDescription(studyMainDicomTags != null ? studyMainDicomTags.get("StudyDescription") : null)
                                .studyInstanceUID(studyMainDicomTags != null ? studyMainDicomTags.get("StudyInstanceUID") : null)
                                .accessionNumber(studyMainDicomTags != null ? studyMainDicomTags.get("AccessionNumber") : null)
                                .isStable((Boolean) studyData.getOrDefault("IsStable", false))
                                .lastUpdate((String) studyData.get("LastUpdate"))
                                .studyMainDicomTags(studyMainDicomTags)
                                .series(seriesDetailsList)
                                .build());
                })
                .onErrorReturn(OrthancPatientDetailResponseDTO.StudyDetailDTO.builder()
                        .id(studyId)
                        .series(new ArrayList<>())
                        .build());
    }
}
