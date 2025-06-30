package sn.xyz.medcenter.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.xyz.medcenter.dto.PathologieDTO;
import sn.xyz.medcenter.model.CategoriePathologie;
import sn.xyz.medcenter.model.ConseilMedical;
import sn.xyz.medcenter.model.Pathologie;
import sn.xyz.medcenter.repository.CategoriePathologieRepository;
import sn.xyz.medcenter.repository.PathologieRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service pour la gestion des pathologies
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PathologieService {
    
    private final PathologieRepository pathologieRepository;
    private final CategoriePathologieRepository categoriePathologieRepository;
    
    /**
     * Récupère toutes les pathologies
     * @return Liste des pathologies
     */
    @Transactional(readOnly = true)
    public List<PathologieDTO> getAllPathologies() {
        log.info("Récupération de toutes les pathologies");
        return pathologieRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Récupère une pathologie par son ID
     * @param id ID de la pathologie
     * @return La pathologie ou vide si non trouvée
     */
    @Transactional(readOnly = true)
    public Optional<PathologieDTO> getPathologieById(Long id) {
        log.info("Récupération de la pathologie avec l'ID: {}", id);
        return pathologieRepository.findById(id)
                .map(this::convertToDTO);
    }
    
    /**
     * Récupère une pathologie par son slug
     * @param slug Slug de la pathologie
     * @return La pathologie ou vide si non trouvée
     */
    @Transactional(readOnly = true)
    public Optional<PathologieDTO> getPathologieBySlug(String slug) {
        log.info("Récupération de la pathologie avec le slug: {}", slug);
        return pathologieRepository.findBySlug(slug)
                .map(this::convertToDTO);
    }
    
    /**
     * Récupère toutes les pathologies d'une catégorie
     * @param categorieId ID de la catégorie
     * @return Liste des pathologies dans cette catégorie
     */
    @Transactional(readOnly = true)
    public List<PathologieDTO> getPathologiesByCategorie(Long categorieId) {
        log.info("Récupération des pathologies pour la catégorie avec l'ID: {}", categorieId);
        return pathologieRepository.findByCategorieId(categorieId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Crée une nouvelle pathologie
     * @param pathologieDTO DTO de la pathologie à créer
     * @return La pathologie créée ou null si la catégorie n'existe pas
     */
    @Transactional
    public Optional<PathologieDTO> createPathologie(PathologieDTO pathologieDTO) {
        log.info("Création d'une nouvelle pathologie: {}", pathologieDTO.getNom());
        
        Optional<CategoriePathologie> categorieOpt = categoriePathologieRepository.findById(pathologieDTO.getCategorieId());
        if (categorieOpt.isEmpty()) {
            log.error("La catégorie avec l'ID: {} n'existe pas", pathologieDTO.getCategorieId());
            return Optional.empty();
        }
        
        Pathologie pathologie = new Pathologie();
        pathologie.setNom(pathologieDTO.getNom());
        pathologie.setSlug(pathologieDTO.getSlug());
        pathologie.setDescription(pathologieDTO.getDescription());
        pathologie.setIcone(pathologieDTO.getIcone());
        pathologie.setCategorie(categorieOpt.get());
        pathologie.setOrdre(pathologieDTO.getOrdre() != null ? pathologieDTO.getOrdre() : 0);
        pathologie.setPubliee(pathologieDTO.getPubliee() != null ? pathologieDTO.getPubliee() : false);
        pathologie.setCreatedAt(LocalDateTime.now());
        pathologie.setUpdatedAt(LocalDateTime.now());
        
        Pathologie savedPathologie = pathologieRepository.save(pathologie);
        return Optional.of(convertToDTO(savedPathologie));
    }
    
    /**
     * Met à jour une pathologie existante
     * @param id ID de la pathologie à mettre à jour
     * @param pathologieDTO DTO avec les nouvelles valeurs
     * @return La pathologie mise à jour ou vide si non trouvée
     */
    @Transactional
    public Optional<PathologieDTO> updatePathologie(Long id, PathologieDTO pathologieDTO) {
        log.info("Mise à jour de la pathologie avec l'ID: {}", id);
        
        Optional<Pathologie> pathologieOpt = pathologieRepository.findById(id);
        if (pathologieOpt.isEmpty()) {
            return Optional.empty();
        }
        
        Optional<CategoriePathologie> categorieOpt = categoriePathologieRepository.findById(pathologieDTO.getCategorieId());
        if (categorieOpt.isEmpty()) {
            log.error("La catégorie avec l'ID: {} n'existe pas", pathologieDTO.getCategorieId());
            return Optional.empty();
        }
        
        Pathologie pathologie = pathologieOpt.get();
        pathologie.setNom(pathologieDTO.getNom());
        pathologie.setSlug(pathologieDTO.getSlug());
        pathologie.setDescription(pathologieDTO.getDescription());
        pathologie.setIcone(pathologieDTO.getIcone());
        pathologie.setCategorie(categorieOpt.get());
        pathologie.setOrdre(pathologieDTO.getOrdre());
        pathologie.setPubliee(pathologieDTO.getPubliee());
        pathologie.setUpdatedAt(LocalDateTime.now());
        
        Pathologie updatedPathologie = pathologieRepository.save(pathologie);
        return Optional.of(convertToDTO(updatedPathologie));
    }
    
    /**
     * Supprime une pathologie
     * @param id ID de la pathologie à supprimer
     */
    @Transactional
    public boolean deletePathologie(Long id) {
        log.info("Suppression de la pathologie avec l'ID: {}", id);
        Optional<Pathologie> pathologieOpt = pathologieRepository.findById(id);
        if (pathologieOpt.isPresent()) {
            pathologieRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    /**
     * Convertit une entité en DTO
     * @param pathologie L'entité à convertir
     * @return Le DTO correspondant
     */
    private PathologieDTO convertToDTO(Pathologie pathologie) {
        PathologieDTO dto = new PathologieDTO();
        dto.setId(pathologie.getId());
        dto.setNom(pathologie.getNom());
        dto.setSlug(pathologie.getSlug());
        dto.setDescription(pathologie.getDescription());
        dto.setIcone(pathologie.getIcone());
        dto.setCategorieId(pathologie.getCategorie().getId());
        dto.setCategorieNom(pathologie.getCategorie().getNom());
        dto.setOrdre(pathologie.getOrdre());
        dto.setPubliee(pathologie.getPubliee());
        dto.setCreatedAt(pathologie.getCreatedAt());
        dto.setUpdatedAt(pathologie.getUpdatedAt());
        
        // Convertir les conseils médicaux associés
        if (pathologie.getConseils() != null) {
            dto.setConseils(pathologie.getConseils().stream()
                    .map(this::convertConseilToShortDTO)
                    .collect(Collectors.toList()));
        } else {
            dto.setConseils(Collections.emptyList());
        }
        
        // Convertir les pathologies associées
        if (pathologie.getPathologiesAssociees() != null) {
            dto.setPathologiesAssociees(pathologie.getPathologiesAssociees().stream()
                    .map(this::convertToAssociatedDTO)
                    .collect(Collectors.toList()));
        } else {
            dto.setPathologiesAssociees(Collections.emptyList());
        }
        
        return dto;
    }
    
    /**
     * Convertit une entité ConseilMedical en DTO simplifié pour éviter les références circulaires
     */
    private PathologieDTO.ConseilMedicalShortDTO convertConseilToShortDTO(ConseilMedical conseil) {
        return PathologieDTO.ConseilMedicalShortDTO.builder()
                .id(conseil.getId())
                .titre(conseil.getTitre())
                .statut(conseil.getStatut().name())
                .build();
    }
    
    /**
     * Convertit une entité Pathologie en DTO simplifié pour les pathologies associées
     */
    private PathologieDTO.PathologieAssocieeDTO convertToAssociatedDTO(Pathologie pathologie) {
        return PathologieDTO.PathologieAssocieeDTO.builder()
                .id(pathologie.getId())
                .nom(pathologie.getNom())
                .slug(pathologie.getSlug())
                .build();
    }
}
