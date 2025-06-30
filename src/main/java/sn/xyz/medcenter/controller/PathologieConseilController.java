package sn.xyz.medcenter.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sn.xyz.medcenter.dto.ConseilMedicalDTO;
import sn.xyz.medcenter.service.ConseilMedicalService;

import java.util.List;

/**
 * Endpoint pour récupérer les conseils médicaux liés à une pathologie
 */
@RestController
@RequestMapping("/api/medical/pathologies")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
@Slf4j
public class PathologieConseilController {

    private final ConseilMedicalService conseilService;
    
    /**
     * Liste tous les conseils médicaux pour une pathologie
     */
    @GetMapping("/{id}/conseils")
    // @PreAuthorize("hasAnyRole('PROFESSIONNEL', 'ADMIN', 'PATIENT')")
    public ResponseEntity<List<ConseilMedicalDTO>> getConseilsByPathologie(@PathVariable Long id) {
        log.info("Requête pour récupérer les conseils médicaux de la pathologie avec l'ID: {}", id);
        return ResponseEntity.ok(conseilService.getConseilsByPathologie(id));
    }
}
