package sn.xyz.medcenter.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sn.xyz.medcenter.dto.ConseilMedicalDTO;
import sn.xyz.medcenter.dto.RecommandationDTO;
import sn.xyz.medcenter.dto.RessourceConseilDTO;
import sn.xyz.medcenter.dto.SectionConseilDTO;
import sn.xyz.medcenter.model.ConseilMedical;
import sn.xyz.medcenter.service.ConseilMedicalService;

import java.util.List;

/**
 * Contrôleur pour la gestion des conseils médicaux
 */
@RestController
@RequestMapping("/api/medical/conseils")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
@Slf4j
public class ConseilMedicalController {

    private final ConseilMedicalService conseilService;
    
    /**
     * Liste tous les conseils médicaux (filtrable par statut)
     */
    @GetMapping
    // @PreAuthorize("hasAnyRole('PROFESSIONNEL', 'ADMIN')")
    public ResponseEntity<List<ConseilMedicalDTO>> getAllConseils(
            @RequestParam(required = false) String statut) {
        log.info("Requête pour récupérer tous les conseils médicaux{}", 
                statut != null ? " avec le statut: " + statut : "");
        
        if (statut != null) {
            try {
                ConseilMedical.StatutConseil statutEnum = ConseilMedical.StatutConseil.valueOf(statut.toUpperCase());
                return ResponseEntity.ok(conseilService.getConseilsByStatut(statutEnum));
            } catch (IllegalArgumentException e) {
                log.warn("Statut invalide: {}", statut);
                return ResponseEntity.badRequest().build();
            }
        }
        
        return ResponseEntity.ok(conseilService.getAllConseils());
    }
    
    /**
     * Récupère les détails d'un conseil médical spécifique
     */
    @GetMapping("/{id}")
    // @PreAuthorize("hasAnyRole('PROFESSIONNEL', 'ADMIN', 'PATIENT')")
    public ResponseEntity<ConseilMedicalDTO> getConseilById(@PathVariable Long id) {
        log.info("Requête pour récupérer le conseil médical avec l'ID: {}", id);
        return conseilService.getConseilById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Liste tous les conseils médicaux publics
     */
    @GetMapping("/publics")
    // @PreAuthorize("hasAnyRole('PROFESSIONNEL', 'ADMIN', 'PATIENT')")
    public ResponseEntity<List<ConseilMedicalDTO>> getPublicConseils() {
        log.info("Requête pour récupérer tous les conseils médicaux publics");
        return ResponseEntity.ok(conseilService.getPublicConseils());
    }
    
    /**
     * Crée un nouveau conseil médical
     */
    @PostMapping
    // @PreAuthorize("hasAnyRole('ADMIN', 'PROFESSIONNEL')")
    public ResponseEntity<ConseilMedicalDTO> createConseil(@RequestBody ConseilMedicalDTO conseilDTO) {
        log.info("Requête pour créer un nouveau conseil médical: {}", conseilDTO.getTitre());
        return conseilService.createConseil(conseilDTO)
                .map(created -> ResponseEntity.status(HttpStatus.CREATED).body(created))
                .orElse(ResponseEntity.badRequest().build());
    }
    
    /**
     * Met à jour un conseil médical existant
     */
    @PutMapping("/{id}")
    // @PreAuthorize("hasAnyRole('ADMIN', 'PROFESSIONNEL')")
    public ResponseEntity<ConseilMedicalDTO> updateConseil(
            @PathVariable Long id,
            @RequestBody ConseilMedicalDTO conseilDTO) {
        log.info("Requête pour mettre à jour le conseil médical avec l'ID: {}", id);
        return conseilService.updateConseil(id, conseilDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Change le statut d'un conseil médical
     */
    @PutMapping("/{id}/statut")
    // @PreAuthorize("hasAnyRole('ADMIN', 'PROFESSIONNEL')")
    public ResponseEntity<ConseilMedicalDTO> changeStatutConseil(
            @PathVariable Long id,
            @RequestBody String statut) {
        log.info("Requête pour changer le statut du conseil médical avec l'ID: {} vers {}", id, statut);
        
        try {
            ConseilMedical.StatutConseil statutEnum = ConseilMedical.StatutConseil.valueOf(statut.toUpperCase().trim());
            return conseilService.changeStatut(id, statutEnum)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            log.warn("Statut invalide: {}", statut);
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Supprime un conseil médical
     */
    @DeleteMapping("/{id}")
    // @PreAuthorize("hasAnyRole('ADMIN', 'PROFESSIONNEL')")
    public ResponseEntity<Void> deleteConseil(@PathVariable Long id) {
        log.info("Requête pour supprimer le conseil médical avec l'ID: {}", id);
        if (conseilService.deleteConseil(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
    
    /**
     * GESTION DES SECTIONS
     */
    
    /**
     * Liste toutes les sections d'un conseil
     */
    @GetMapping("/{id}/sections")
    // @PreAuthorize("hasAnyRole('PROFESSIONNEL', 'ADMIN', 'PATIENT')")
    public ResponseEntity<List<SectionConseilDTO>> getSectionsByConseil(@PathVariable Long id) {
        log.info("Requête pour récupérer les sections du conseil avec l'ID: {}", id);
        return ResponseEntity.ok(conseilService.getSectionsByConseil(id));
    }
    
    /**
     * Ajoute une section à un conseil
     */
    @PostMapping("/{id}/sections")
    // @PreAuthorize("hasAnyRole('ADMIN', 'PROFESSIONNEL')")
    public ResponseEntity<SectionConseilDTO> addSection(
            @PathVariable Long id,
            @RequestBody SectionConseilDTO sectionDTO) {
        log.info("Requête pour ajouter une section au conseil avec l'ID: {}", id);
        return conseilService.addSection(id, sectionDTO)
                .map(created -> ResponseEntity.status(HttpStatus.CREATED).body(created))
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Met à jour une section
     */
    @PutMapping("/sections/{id}")
    // @PreAuthorize("hasAnyRole('ADMIN', 'PROFESSIONNEL')")
    public ResponseEntity<SectionConseilDTO> updateSection(
            @PathVariable Long id,
            @RequestBody SectionConseilDTO sectionDTO) {
        log.info("Requête pour mettre à jour la section avec l'ID: {}", id);
        return conseilService.updateSection(id, sectionDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Supprime une section
     */
    @DeleteMapping("/sections/{id}")
    // @PreAuthorize("hasAnyRole('ADMIN', 'PROFESSIONNEL')")
    public ResponseEntity<Void> deleteSection(@PathVariable Long id) {
        log.info("Requête pour supprimer la section avec l'ID: {}", id);
        if (conseilService.deleteSection(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
    
    /**
     * GESTION DES RESSOURCES
     */
    
    /**
     * Liste toutes les ressources d'un conseil
     */
    @GetMapping("/{id}/ressources")
    // @PreAuthorize("hasAnyRole('PROFESSIONNEL', 'ADMIN', 'PATIENT')")
    public ResponseEntity<List<RessourceConseilDTO>> getRessourcesByConseil(@PathVariable Long id) {
        log.info("Requête pour récupérer les ressources du conseil avec l'ID: {}", id);
        return ResponseEntity.ok(conseilService.getRessourcesByConseil(id));
    }
    
    /**
     * Ajoute une ressource à un conseil
     */
    @PostMapping("/{id}/ressources")
    // @PreAuthorize("hasAnyRole('ADMIN', 'PROFESSIONNEL')")
    public ResponseEntity<RessourceConseilDTO> addRessource(
            @PathVariable Long id,
            @RequestBody RessourceConseilDTO ressourceDTO) {
        log.info("Requête pour ajouter une ressource au conseil avec l'ID: {}", id);
        return conseilService.addRessource(id, ressourceDTO)
                .map(created -> ResponseEntity.status(HttpStatus.CREATED).body(created))
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Met à jour une ressource
     */
    @PutMapping("/ressources/{id}")
    // @PreAuthorize("hasAnyRole('ADMIN', 'PROFESSIONNEL')")
    public ResponseEntity<RessourceConseilDTO> updateRessource(
            @PathVariable Long id,
            @RequestBody RessourceConseilDTO ressourceDTO) {
        log.info("Requête pour mettre à jour la ressource avec l'ID: {}", id);
        return conseilService.updateRessource(id, ressourceDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Supprime une ressource
     */
    @DeleteMapping("/ressources/{id}")
    // @PreAuthorize("hasAnyRole('ADMIN', 'PROFESSIONNEL')")
    public ResponseEntity<Void> deleteRessource(@PathVariable Long id) {
        log.info("Requête pour supprimer la ressource avec l'ID: {}", id);
        if (conseilService.deleteRessource(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
    
    /**
     * GESTION DES RECOMMANDATIONS
     */
    
    /**
     * Liste toutes les recommandations d'un conseil
     */
    @GetMapping("/{id}/recommandations")
    // @PreAuthorize("hasAnyRole('PROFESSIONNEL', 'ADMIN', 'PATIENT')")
    public ResponseEntity<List<RecommandationDTO>> getRecommandationsByConseil(@PathVariable Long id) {
        log.info("Requête pour récupérer les recommandations du conseil avec l'ID: {}", id);
        return ResponseEntity.ok(conseilService.getRecommandationsByConseil(id));
    }
    
    /**
     * Ajoute une recommandation à un conseil
     */
    @PostMapping("/{id}/recommandations")
    // @PreAuthorize("hasAnyRole('ADMIN', 'PROFESSIONNEL')")
    public ResponseEntity<RecommandationDTO> addRecommandation(
            @PathVariable Long id,
            @RequestBody RecommandationDTO recommandationDTO) {
        log.info("Requête pour ajouter une recommandation au conseil avec l'ID: {}", id);
        return conseilService.addRecommandation(id, recommandationDTO)
                .map(created -> ResponseEntity.status(HttpStatus.CREATED).body(created))
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Met à jour une recommandation
     */
    @PutMapping("/recommandations/{id}")
    // @PreAuthorize("hasAnyRole('ADMIN', 'PROFESSIONNEL')")
    public ResponseEntity<RecommandationDTO> updateRecommandation(
            @PathVariable Long id,
            @RequestBody RecommandationDTO recommandationDTO) {
        log.info("Requête pour mettre à jour la recommandation avec l'ID: {}", id);
        return conseilService.updateRecommandation(id, recommandationDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Supprime une recommandation
     */
    @DeleteMapping("/recommandations/{id}")
    // @PreAuthorize("hasAnyRole('ADMIN', 'PROFESSIONNEL')")
    public ResponseEntity<Void> deleteRecommandation(@PathVariable Long id) {
        log.info("Requête pour supprimer la recommandation avec l'ID: {}", id);
        if (conseilService.deleteRecommandation(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
