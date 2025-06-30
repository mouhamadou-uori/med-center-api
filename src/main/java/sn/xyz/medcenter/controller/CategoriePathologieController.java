package sn.xyz.medcenter.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sn.xyz.medcenter.dto.CategoriePathologieDTO;
import sn.xyz.medcenter.dto.PathologieDTO;
import sn.xyz.medcenter.service.CategoriePathologieService;
import sn.xyz.medcenter.service.PathologieService;

import java.util.List;

/**
 * Contrôleur pour la gestion des catégories de pathologies
 */
@RestController
@RequestMapping("/api/medical/categories")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
@Slf4j
public class CategoriePathologieController {

    private final CategoriePathologieService categorieService;
    private final PathologieService pathologieService;
    
    /**
     * Liste toutes les catégories de pathologies
     */
    @GetMapping
    // @PreAuthorize("hasAnyRole('PROFESSIONNEL', 'ADMIN', 'PATIENT')")
    public ResponseEntity<List<CategoriePathologieDTO>> getAllCategories() {
        log.info("Requête pour récupérer toutes les catégories de pathologies");
        return ResponseEntity.ok(categorieService.getAllCategories());
    }
    
    /**
     * Récupère les détails d'une catégorie spécifique
     */
    @GetMapping("/{id}")
    // @PreAuthorize("hasAnyRole('PROFESSIONNEL', 'ADMIN', 'PATIENT')")
    public ResponseEntity<CategoriePathologieDTO> getCategorieById(@PathVariable Long id) {
        log.info("Requête pour récupérer la catégorie avec l'ID: {}", id);
        return categorieService.getCategorieById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Crée une nouvelle catégorie de pathologie
     */
    @PostMapping
    // @PreAuthorize("hasAnyRole('ADMIN', 'PROFESSIONNEL')")
    public ResponseEntity<CategoriePathologieDTO> createCategorie(@RequestBody CategoriePathologieDTO categorieDTO) {
        log.info("Requête pour créer une nouvelle catégorie de pathologie: {}", categorieDTO.getNom());
        return ResponseEntity.status(HttpStatus.CREATED).body(categorieService.createCategorie(categorieDTO));
    }
    
    /**
     * Met à jour une catégorie existante
     */
    @PutMapping("/{id}")
    // @PreAuthorize("hasAnyRole('ADMIN', 'PROFESSIONNEL')")
    public ResponseEntity<CategoriePathologieDTO> updateCategorie(
            @PathVariable Long id,
            @RequestBody CategoriePathologieDTO categorieDTO) {
        log.info("Requête pour mettre à jour la catégorie avec l'ID: {}", id);
        return categorieService.updateCategorie(id, categorieDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Supprime une catégorie
     */
    @DeleteMapping("/{id}")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCategorie(@PathVariable Long id) {
        log.info("Requête pour supprimer la catégorie avec l'ID: {}", id);
        if (categorieService.deleteCategorie(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
    
    /**
     * Récupère toutes les pathologies d'une catégorie
     */
    @GetMapping("/{id}/pathologies")
    // @PreAuthorize("hasAnyRole('PROFESSIONNEL', 'ADMIN', 'PATIENT')")
    public ResponseEntity<List<PathologieDTO>> getPathologiesByCategorie(@PathVariable Long id) {
        log.info("Requête pour récupérer les pathologies de la catégorie avec l'ID: {}", id);
        return ResponseEntity.ok(pathologieService.getPathologiesByCategorie(id));
    }
}
