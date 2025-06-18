package sn.xyz.medcenter.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;
import sn.xyz.medcenter.model.Etude;
import sn.xyz.medcenter.model.Instance;
import sn.xyz.medcenter.model.PatientOrthanc;
import sn.xyz.medcenter.model.Serie;
import sn.xyz.medcenter.service.OrthancService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orthanc")
// @CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
@Slf4j
public class OrthancController {

    private final OrthancService orthancService;

    /**
     * Récupère tous les patients
     * Accessible aux professionnels de santé, radiologues et administrateurs
     */
    @GetMapping(value = "/patients", produces = MediaType.APPLICATION_JSON_VALUE)
    // @PreAuthorize("hasAnyRole('PROFESSIONNEL', 'RADIOLOGUE', 'ADMIN')")
    public Mono<String[]> getAllPatients() {
        log.info("Requête: récupérer tous les patients");
        return orthancService.getPatients()
                .doOnSuccess(patients -> {
                    if (patients != null) {
                        log.info("Réponse envoyée avec {} patients", patients.length);
                    }
                })
                .defaultIfEmpty(new String[0]);
    }

    /**
     * Récupère les détails d'un patient spécifique
     * Accessible aux professionnels de santé et radiologues
     */
    @GetMapping("/patients/{id}")
    @PreAuthorize("hasAnyRole('PROFESSIONNEL', 'RADIOLOGUE')")
    public Mono<ResponseEntity<PatientOrthanc>> getPatient(@PathVariable String id) {
        log.info("Requête: récupérer le patient avec ID: {}", id);
        return orthancService.getPatient(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * Récupère toutes les études pour un patient
     * Accessible aux professionnels de santé et radiologues
     */
    @GetMapping("/patients/{id}/studies")
    @PreAuthorize("hasAnyRole('PROFESSIONNEL', 'RADIOLOGUE')")
    public Mono<ResponseEntity<List<Etude>>> getPatientStudies(@PathVariable String id) {
        log.info("Requête: récupérer les études pour le patient ID: {}", id);
        return orthancService.getStudies(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * Récupère les détails d'une étude spécifique
     * Nécessite le rôle ADMIN
     */
    @GetMapping("/studies/{id}")
    @PreAuthorize("hasAnyRole('PROFESSIONNEL', 'RADIOLOGUE')")
    public Mono<ResponseEntity<Etude>> getStudy(@PathVariable String id) {
        log.info("Requête: récupérer l'étude avec ID: {}", id);
        return orthancService.getStudy(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * Récupère toutes les séries pour une étude
     * Nécessite le rôle ADMIN
     */
    @GetMapping("/studies/{id}/series")
    @PreAuthorize("hasAnyRole('PROFESSIONNEL', 'RADIOLOGUE')")
    public Mono<ResponseEntity<List<Serie>>> getStudySeries(@PathVariable String id) {
        log.info("Requête: récupérer les séries pour l'étude ID: {}", id);
        return orthancService.getSeries(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * Récupère les statistiques du serveur Orthanc
     * Nécessite le rôle ADMIN
     */
    @GetMapping("/statistics")
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<ResponseEntity<Map<String, Object>>> getOrthancStatistics() {
        log.info("Requête: récupérer les statistiques du serveur Orthanc");
        return orthancService.getStatistics()
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * Récupère les informations système du serveur Orthanc
     * Nécessite le rôle ADMIN
     */
    @GetMapping("/system")
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<ResponseEntity<Map<String, Object>>> getOrthancSystemInfo() {
        log.info("Requête: récupérer les informations système du serveur Orthanc");
        return orthancService.getSystemInfo()
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * Upload d'une nouvelle instance DICOM
     * Nécessite le rôle PROFESSIONNEL
     */
    @PostMapping("/instances")
    @PreAuthorize("hasRole('RADIOLOGUE')")
    public Mono<ResponseEntity<Instance>> uploadInstance(@RequestBody byte[] dicomData) {
        log.info("Requête: upload d'une nouvelle instance DICOM");
        return orthancService.uploadInstance(dicomData)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    /**
     * Export d'une étude vers un autre PACS
     * Nécessite le rôle PROFESSIONNEL
     */
    @PostMapping("/studies/{id}/export")
    @PreAuthorize("hasRole('RADIOLOGUE')")
    public Mono<ResponseEntity<Map<String, Object>>> exportStudy(@PathVariable String id, @RequestParam String targetPacs) {
        log.info("Requête: export de l'étude {} vers {}", id, targetPacs);
        return orthancService.exportStudy(id, targetPacs)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    /**
     * Recherche DICOM
     * Nécessite le rôle PROFESSIONNEL ou ADMIN
     */
    @PostMapping("/tools/find")
    @PreAuthorize("hasAnyRole('PROFESSIONNEL', 'RADIOLOGUE')")
    public Mono<ResponseEntity<List<Etude>>> findDicom(@RequestBody Map<String, Object> query) {
        log.info("Requête: recherche DICOM avec critères: {}", query);
        return orthancService.findDicom(query)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
