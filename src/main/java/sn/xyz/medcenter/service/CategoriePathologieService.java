package sn.xyz.medcenter.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.xyz.medcenter.dto.CategoriePathologieDTO;
import sn.xyz.medcenter.model.CategoriePathologie;
import sn.xyz.medcenter.model.Pathologie;
import sn.xyz.medcenter.repository.CategoriePathologieRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service pour la gestion des catégories de pathologies
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CategoriePathologieService {
    
    private final CategoriePathologieRepository categoriePathologieRepository;
    
    /**
     * Récupère toutes les catégories
     * @return Liste des catégories
     */
    @Transactional(readOnly = true)
    public List<CategoriePathologieDTO> getAllCategories() {
        log.info("Récupération de toutes les catégories de pathologies");
        return categoriePathologieRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Récupère toutes les catégories actives
     * @return Liste des catégories actives
     */
    @Transactional(readOnly = true)
    public List<CategoriePathologieDTO> getAllActiveCategories() {
        log.info("Récupération des catégories de pathologies actives");
        return categoriePathologieRepository.findByActiveTrue().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Récupère une catégorie par son ID
     * @param id ID de la catégorie
     * @return La catégorie ou vide si non trouvée
     */
    @Transactional(readOnly = true)
    public Optional<CategoriePathologieDTO> getCategorieById(Long id) {
        log.info("Récupération de la catégorie avec l'ID: {}", id);
        return categoriePathologieRepository.findById(id)
                .map(this::convertToDTO);
    }
    
    /**
     * Crée une nouvelle catégorie
     * @param categorieDTO DTO de la catégorie à créer
     * @return La catégorie créée
     */
    @Transactional
    public CategoriePathologieDTO createCategorie(CategoriePathologieDTO categorieDTO) {
        log.info("Création d'une nouvelle catégorie de pathologie: {}", categorieDTO.getNom());
        CategoriePathologie categorie = new CategoriePathologie();
        categorie.setNom(categorieDTO.getNom());
        categorie.setDescription(categorieDTO.getDescription());
        categorie.setIcone(categorieDTO.getIcone());
        categorie.setOrdre(categorieDTO.getOrdre() != null ? categorieDTO.getOrdre() : 0);
        categorie.setActive(categorieDTO.getActive() != null ? categorieDTO.getActive() : true);
        
        CategoriePathologie savedCategorie = categoriePathologieRepository.save(categorie);
        return convertToDTO(savedCategorie);
    }
    
    /**
     * Met à jour une catégorie existante
     * @param id ID de la catégorie à mettre à jour
     * @param categorieDTO DTO avec les nouvelles valeurs
     * @return La catégorie mise à jour ou vide si non trouvée
     */
    @Transactional
    public Optional<CategoriePathologieDTO> updateCategorie(Long id, CategoriePathologieDTO categorieDTO) {
        log.info("Mise à jour de la catégorie avec l'ID: {}", id);
        return categoriePathologieRepository.findById(id)
                .map(categorie -> {
                    categorie.setNom(categorieDTO.getNom());
                    categorie.setDescription(categorieDTO.getDescription());
                    categorie.setIcone(categorieDTO.getIcone());
                    categorie.setOrdre(categorieDTO.getOrdre());
                    categorie.setActive(categorieDTO.getActive());
                    return convertToDTO(categoriePathologieRepository.save(categorie));
                });
    }
    
    /**
     * Supprime une catégorie
     * @param id ID de la catégorie à supprimer
     */
    @Transactional
    public boolean deleteCategorie(Long id) {
        log.info("Suppression de la catégorie avec l'ID: {}", id);
        Optional<CategoriePathologie> categorieOpt = categoriePathologieRepository.findById(id);
        if (categorieOpt.isPresent()) {
            categoriePathologieRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    /**
     * Convertit une entité en DTO
     * @param categorie L'entité à convertir
     * @return Le DTO correspondant
     */
    private CategoriePathologieDTO convertToDTO(CategoriePathologie categorie) {
        CategoriePathologieDTO dto = new CategoriePathologieDTO();
        dto.setId(categorie.getId());
        dto.setNom(categorie.getNom());
        dto.setDescription(categorie.getDescription());
        dto.setIcone(categorie.getIcone());
        dto.setOrdre(categorie.getOrdre());
        dto.setActive(categorie.getActive());
        
        if (categorie.getPathologies() != null) {
            dto.setPathologies(categorie.getPathologies().stream()
                    .map(this::convertToShortDTO)
                    .collect(Collectors.toList()));
        }
        
        return dto;
    }
    
    /**
     * Convertit une entité Pathologie en DTO simplifié pour éviter les références circulaires
     */
    private CategoriePathologieDTO.PathologieShortDTO convertToShortDTO(Pathologie pathologie) {
        return CategoriePathologieDTO.PathologieShortDTO.builder()
                .id(pathologie.getId())
                .nom(pathologie.getNom())
                .slug(pathologie.getSlug())
                .build();
    }
}
