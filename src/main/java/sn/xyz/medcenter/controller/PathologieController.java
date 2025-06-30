package sn.xyz.medcenter.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sn.xyz.medcenter.dto.PathologieDTO;
import sn.xyz.medcenter.service.PathologieService;

import java.util.List;

/**
 * Contrôleur pour la gestion des pathologies
 */
@RestController
@RequestMapping("/api/medical/pathologies")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
@Slf4j
public class PathologieController {

    private final PathologieService pathologieService;
    
    /**
     * Liste toutes les pathologies
     */
    @GetMapping
    // @PreAuthorize("hasAnyRole('PROFESSIONNEL', 'ADMIN', 'PATIENT')")
    public ResponseEntity<List<PathologieDTO>> getAllPathologies() {
        log.info("Requête pour récupérer toutes les pathologies");
        return ResponseEntity.ok(pathologieService.getAllPathologies());
    }
    
    /**
     * Récupère les détails d'une pathologie spécifique
     */
    @GetMapping("/{id}")
    // @PreAuthorize("hasAnyRole('PROFESSIONNEL', 'ADMIN', 'PATIENT')")
    public ResponseEntity<PathologieDTO> getPathologieById(@PathVariable Long id) {
        log.info("Requête pour récupérer la pathologie avec l'ID: {}", id);
        return pathologieService.getPathologieById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Récupère une pathologie par son slug
     */
    @GetMapping("/slug/{slug}")
    // @PreAuthorize("hasAnyRole('PROFESSIONNEL', 'ADMIN', 'PATIENT')")
    public ResponseEntity<PathologieDTO> getPathologieBySlug(@PathVariable String slug) {
        log.info("Requête pour récupérer la pathologie avec le slug: {}", slug);
        return pathologieService.getPathologieBySlug(slug)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Crée une nouvelle pathologie
     */
    @PostMapping
    // @PreAuthorize("hasAnyRole('ADMIN', 'PROFESSIONNEL')")
    public ResponseEntity<PathologieDTO> createPathologie(@RequestBody PathologieDTO pathologieDTO) {
        log.info("Requête pour créer une nouvelle pathologie: {}", pathologieDTO.getNom());
        return pathologieService.createPathologie(pathologieDTO)
                .map(created -> ResponseEntity.status(HttpStatus.CREATED).body(created))
                .orElse(ResponseEntity.badRequest().build());
    }
    
    /**
     * Met à jour une pathologie existante
     */
    @PutMapping("/{id}")
    // @PreAuthorize("hasAnyRole('ADMIN', 'PROFESSIONNEL')")
    public ResponseEntity<PathologieDTO> updatePathologie(
            @PathVariable Long id,
            @RequestBody PathologieDTO pathologieDTO) {
        log.info("Requête pour mettre à jour la pathologie avec l'ID: {}", id);
        return pathologieService.updatePathologie(id, pathologieDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Supprime une pathologie
     */
    @DeleteMapping("/{id}")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePathologie(@PathVariable Long id) {
        log.info("Requête pour supprimer la pathologie avec l'ID: {}", id);
        if (pathologieService.deletePathologie(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
