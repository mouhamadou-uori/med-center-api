package sn.xyz.medcenter.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.xyz.medcenter.dto.ConseilMedicalDTO;
import sn.xyz.medcenter.dto.RecommandationDTO;
import sn.xyz.medcenter.dto.RessourceConseilDTO;
import sn.xyz.medcenter.dto.SectionConseilDTO;
import sn.xyz.medcenter.model.*;
import sn.xyz.medcenter.repository.ConseilMedicalRepository;
import sn.xyz.medcenter.repository.PathologieRepository;
import sn.xyz.medcenter.repository.ProfessionnelSanteRepository;
import sn.xyz.medcenter.repository.RecommandationRepository;
import sn.xyz.medcenter.repository.RessourceConseilRepository;
import sn.xyz.medcenter.repository.SectionConseilRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service pour la gestion des conseils médicaux
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ConseilMedicalService {
    
    private final ConseilMedicalRepository conseilRepository;
    private final PathologieRepository pathologieRepository;
    private final ProfessionnelSanteRepository professionnelRepository;
    private final SectionConseilRepository sectionRepository;
    private final RessourceConseilRepository ressourceRepository;
    private final RecommandationRepository recommandationRepository;
    
    /**
     * Récupère tous les conseils médicaux
     * @return Liste des conseils médicaux
     */
    @Transactional(readOnly = true)
    public List<ConseilMedicalDTO> getAllConseils() {
        log.info("Récupération de tous les conseils médicaux");
        return conseilRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Récupère les conseils médicaux par statut
     * @param statut Le statut recherché
     * @return Liste des conseils médicaux avec ce statut
     */
    @Transactional(readOnly = true)
    public List<ConseilMedicalDTO> getConseilsByStatut(ConseilMedical.StatutConseil statut) {
        log.info("Récupération des conseils médicaux avec le statut: {}", statut);
        return conseilRepository.findByStatut(statut).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Récupère un conseil médical par son ID
     * @param id ID du conseil
     * @return Le conseil ou vide si non trouvé
     */
    @Transactional(readOnly = true)
    public Optional<ConseilMedicalDTO> getConseilById(Long id) {
        log.info("Récupération du conseil médical avec l'ID: {}", id);
        return conseilRepository.findById(id)
                .map(this::convertToDTO);
    }
    
    /**
     * Récupère les conseils médicaux pour une pathologie
     * @param pathologieId ID de la pathologie
     * @return Liste des conseils médicaux pour cette pathologie
     */
    @Transactional(readOnly = true)
    public List<ConseilMedicalDTO> getConseilsByPathologie(Long pathologieId) {
        log.info("Récupération des conseils médicaux pour la pathologie avec l'ID: {}", pathologieId);
        return conseilRepository.findByPathologieId(pathologieId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Récupère tous les conseils médicaux publics
     * @return Liste des conseils médicaux publics
     */
    @Transactional(readOnly = true)
    public List<ConseilMedicalDTO> getPublicConseils() {
        log.info("Récupération des conseils médicaux publics");
        return conseilRepository.findByVisiblePublicTrueAndStatut(ConseilMedical.StatutConseil.PUBLIE).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Crée un nouveau conseil médical
     * @param conseilDTO DTO du conseil à créer
     * @return Le conseil créé ou empty si la pathologie ou l'auteur n'existe pas
     */
    @Transactional
    public Optional<ConseilMedicalDTO> createConseil(ConseilMedicalDTO conseilDTO) {
        log.info("Création d'un nouveau conseil médical: {}", conseilDTO.getTitre());
        
        Optional<Pathologie> pathologieOpt = pathologieRepository.findById(conseilDTO.getPathologieId());
        if (pathologieOpt.isEmpty()) {
            log.error("La pathologie avec l'ID: {} n'existe pas", conseilDTO.getPathologieId());
            return Optional.empty();
        }
        
        Optional<ProfessionnelSante> auteurOpt = professionnelRepository.findById(conseilDTO.getAuteurId().intValue());
        if (auteurOpt.isEmpty()) {
            log.error("Le professionnel de santé avec l'ID: {} n'existe pas", conseilDTO.getAuteurId());
            return Optional.empty();
        }
        
        ConseilMedical conseil = new ConseilMedical();
        conseil.setTitre(conseilDTO.getTitre());
        conseil.setContenu(conseilDTO.getContenu());
        conseil.setResume(conseilDTO.getResume());
        conseil.setMotsCles(conseilDTO.getMotsCles());
        conseil.setPathologie(pathologieOpt.get());
        conseil.setAuteur(auteurOpt.get());
        conseil.setDateCreation(LocalDateTime.now());
        conseil.setDateModification(LocalDateTime.now());
        conseil.setStatut(ConseilMedical.StatutConseil.BROUILLON);
        conseil.setVisiblePublic(conseilDTO.getVisiblePublic() != null ? conseilDTO.getVisiblePublic() : false);
        
        if (conseilDTO.getApprouveParId() != null) {
            Optional<ProfessionnelSante> approbateurOpt = professionnelRepository.findById(conseilDTO.getApprouveParId().intValue());
            approbateurOpt.ifPresent(conseil::setApprouvePar);
        }
        
        ConseilMedical savedConseil = conseilRepository.save(conseil);
        return Optional.of(convertToDTO(savedConseil));
    }
    
    /**
     * Met à jour un conseil médical
     * @param id ID du conseil à mettre à jour
     * @param conseilDTO DTO avec les nouvelles valeurs
     * @return Le conseil mis à jour ou vide si non trouvé
     */
    @Transactional
    public Optional<ConseilMedicalDTO> updateConseil(Long id, ConseilMedicalDTO conseilDTO) {
        log.info("Mise à jour du conseil médical avec l'ID: {}", id);
        
        Optional<ConseilMedical> conseilOpt = conseilRepository.findById(id);
        if (conseilOpt.isEmpty()) {
            return Optional.empty();
        }
        
        ConseilMedical conseil = conseilOpt.get();
        
        if (conseilDTO.getPathologieId() != null && !conseilDTO.getPathologieId().equals(conseil.getPathologie().getId())) {
            Optional<Pathologie> pathologieOpt = pathologieRepository.findById(conseilDTO.getPathologieId());
            if (pathologieOpt.isEmpty()) {
                log.error("La pathologie avec l'ID: {} n'existe pas", conseilDTO.getPathologieId());
                return Optional.empty();
            }
            conseil.setPathologie(pathologieOpt.get());
        }
        
        conseil.setTitre(conseilDTO.getTitre());
        conseil.setContenu(conseilDTO.getContenu());
        conseil.setResume(conseilDTO.getResume());
        conseil.setMotsCles(conseilDTO.getMotsCles());
        conseil.setDateModification(LocalDateTime.now());
        conseil.setVisiblePublic(conseilDTO.getVisiblePublic() != null ? conseilDTO.getVisiblePublic() : conseil.getVisiblePublic());
        
        if (conseilDTO.getApprouveParId() != null) {
            Optional<ProfessionnelSante> approbateurOpt = professionnelRepository.findById(conseilDTO.getApprouveParId().intValue());
            if (approbateurOpt.isPresent()) {
                conseil.setApprouvePar(approbateurOpt.get());
            }
        }
        
        ConseilMedical updatedConseil = conseilRepository.save(conseil);
        return Optional.of(convertToDTO(updatedConseil));
    }
    
    /**
     * Change le statut d'un conseil médical
     * @param id ID du conseil
     * @param statut Nouveau statut
     * @return Le conseil mis à jour ou vide si non trouvé
     */
    @Transactional
    public Optional<ConseilMedicalDTO> changeStatut(Long id, ConseilMedical.StatutConseil statut) {
        log.info("Changement du statut du conseil médical avec l'ID: {} vers {}", id, statut);
        
        Optional<ConseilMedical> conseilOpt = conseilRepository.findById(id);
        if (conseilOpt.isEmpty()) {
            return Optional.empty();
        }
        
        ConseilMedical conseil = conseilOpt.get();
        conseil.setStatut(statut);
        
        if (statut == ConseilMedical.StatutConseil.PUBLIE) {
            conseil.setDatePublication(LocalDateTime.now());
        }
        
        conseil.setDateModification(LocalDateTime.now());
        
        ConseilMedical updatedConseil = conseilRepository.save(conseil);
        return Optional.of(convertToDTO(updatedConseil));
    }
    
    /**
     * Supprime un conseil médical
     * @param id ID du conseil à supprimer
     * @return vrai si supprimé, faux sinon
     */
    @Transactional
    public boolean deleteConseil(Long id) {
        log.info("Suppression du conseil médical avec l'ID: {}", id);
        Optional<ConseilMedical> conseilOpt = conseilRepository.findById(id);
        if (conseilOpt.isPresent()) {
            conseilRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    /**
     * GESTION DES SECTIONS
     */
    
    /**
     * Récupère toutes les sections d'un conseil
     */
    @Transactional(readOnly = true)
    public List<SectionConseilDTO> getSectionsByConseil(Long conseilId) {
        log.info("Récupération des sections pour le conseil avec l'ID: {}", conseilId);
        return sectionRepository.findByConseilIdOrderByOrdre(conseilId).stream()
                .map(this::convertSectionToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Ajoute une section à un conseil
     */
    @Transactional
    public Optional<SectionConseilDTO> addSection(Long conseilId, SectionConseilDTO sectionDTO) {
        log.info("Ajout d'une section au conseil avec l'ID: {}", conseilId);
        
        Optional<ConseilMedical> conseilOpt = conseilRepository.findById(conseilId);
        if (conseilOpt.isEmpty()) {
            return Optional.empty();
        }
        
        SectionConseil section = new SectionConseil();
        section.setTitre(sectionDTO.getTitre());
        section.setContenu(sectionDTO.getContenu());
        section.setOrdre(sectionDTO.getOrdre());
        section.setConseil(conseilOpt.get());
        
        SectionConseil savedSection = sectionRepository.save(section);
        return Optional.of(convertSectionToDTO(savedSection));
    }
    
    /**
     * Met à jour une section
     */
    @Transactional
    public Optional<SectionConseilDTO> updateSection(Long id, SectionConseilDTO sectionDTO) {
        log.info("Mise à jour de la section avec l'ID: {}", id);
        
        Optional<SectionConseil> sectionOpt = sectionRepository.findById(id);
        if (sectionOpt.isEmpty()) {
            return Optional.empty();
        }
        
        SectionConseil section = sectionOpt.get();
        section.setTitre(sectionDTO.getTitre());
        section.setContenu(sectionDTO.getContenu());
        section.setOrdre(sectionDTO.getOrdre());
        
        SectionConseil updatedSection = sectionRepository.save(section);
        return Optional.of(convertSectionToDTO(updatedSection));
    }
    
    /**
     * Supprime une section
     */
    @Transactional
    public boolean deleteSection(Long id) {
        log.info("Suppression de la section avec l'ID: {}", id);
        Optional<SectionConseil> sectionOpt = sectionRepository.findById(id);
        if (sectionOpt.isPresent()) {
            sectionRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    /**
     * GESTION DES RESSOURCES
     */
    
    /**
     * Récupère toutes les ressources d'un conseil
     */
    @Transactional(readOnly = true)
    public List<RessourceConseilDTO> getRessourcesByConseil(Long conseilId) {
        log.info("Récupération des ressources pour le conseil avec l'ID: {}", conseilId);
        return ressourceRepository.findByConseilId(conseilId).stream()
                .map(this::convertRessourceToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Ajoute une ressource à un conseil
     */
    @Transactional
    public Optional<RessourceConseilDTO> addRessource(Long conseilId, RessourceConseilDTO ressourceDTO) {
        log.info("Ajout d'une ressource au conseil avec l'ID: {}", conseilId);
        
        Optional<ConseilMedical> conseilOpt = conseilRepository.findById(conseilId);
        if (conseilOpt.isEmpty()) {
            return Optional.empty();
        }
        
        RessourceConseil ressource = new RessourceConseil();
        ressource.setTitre(ressourceDTO.getTitre());
        ressource.setType(RessourceConseil.TypeRessource.valueOf(ressourceDTO.getType()));
        ressource.setUrl(ressourceDTO.getUrl());
        ressource.setDescription(ressourceDTO.getDescription());
        ressource.setConseil(conseilOpt.get());
        
        RessourceConseil savedRessource = ressourceRepository.save(ressource);
        return Optional.of(convertRessourceToDTO(savedRessource));
    }
    
    /**
     * Met à jour une ressource
     */
    @Transactional
    public Optional<RessourceConseilDTO> updateRessource(Long id, RessourceConseilDTO ressourceDTO) {
        log.info("Mise à jour de la ressource avec l'ID: {}", id);
        
        Optional<RessourceConseil> ressourceOpt = ressourceRepository.findById(id);
        if (ressourceOpt.isEmpty()) {
            return Optional.empty();
        }
        
        RessourceConseil ressource = ressourceOpt.get();
        ressource.setTitre(ressourceDTO.getTitre());
        ressource.setType(RessourceConseil.TypeRessource.valueOf(ressourceDTO.getType()));
        ressource.setUrl(ressourceDTO.getUrl());
        ressource.setDescription(ressourceDTO.getDescription());
        
        RessourceConseil updatedRessource = ressourceRepository.save(ressource);
        return Optional.of(convertRessourceToDTO(updatedRessource));
    }
    
    /**
     * Supprime une ressource
     */
    @Transactional
    public boolean deleteRessource(Long id) {
        log.info("Suppression de la ressource avec l'ID: {}", id);
        Optional<RessourceConseil> ressourceOpt = ressourceRepository.findById(id);
        if (ressourceOpt.isPresent()) {
            ressourceRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    /**
     * GESTION DES RECOMMANDATIONS
     */
    
    /**
     * Récupère toutes les recommandations d'un conseil
     */
    @Transactional(readOnly = true)
    public List<RecommandationDTO> getRecommandationsByConseil(Long conseilId) {
        log.info("Récupération des recommandations pour le conseil avec l'ID: {}", conseilId);
        return recommandationRepository.findByConseilIdOrderByOrdre(conseilId).stream()
                .map(this::convertRecommandationToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Ajoute une recommandation à un conseil
     */
    @Transactional
    public Optional<RecommandationDTO> addRecommandation(Long conseilId, RecommandationDTO recommandationDTO) {
        log.info("Ajout d'une recommandation au conseil avec l'ID: {}", conseilId);
        
        Optional<ConseilMedical> conseilOpt = conseilRepository.findById(conseilId);
        if (conseilOpt.isEmpty()) {
            return Optional.empty();
        }
        
        Recommandation recommandation = new Recommandation();
        recommandation.setTexte(recommandationDTO.getTexte());
        recommandation.setOrdre(recommandationDTO.getOrdre());
        recommandation.setConseil(conseilOpt.get());
        
        Recommandation savedRecommandation = recommandationRepository.save(recommandation);
        return Optional.of(convertRecommandationToDTO(savedRecommandation));
    }
    
    /**
     * Met à jour une recommandation
     */
    @Transactional
    public Optional<RecommandationDTO> updateRecommandation(Long id, RecommandationDTO recommandationDTO) {
        log.info("Mise à jour de la recommandation avec l'ID: {}", id);
        
        Optional<Recommandation> recommandationOpt = recommandationRepository.findById(id);
        if (recommandationOpt.isEmpty()) {
            return Optional.empty();
        }
        
        Recommandation recommandation = recommandationOpt.get();
        recommandation.setTexte(recommandationDTO.getTexte());
        recommandation.setOrdre(recommandationDTO.getOrdre());
        
        Recommandation updatedRecommandation = recommandationRepository.save(recommandation);
        return Optional.of(convertRecommandationToDTO(updatedRecommandation));
    }
    
    /**
     * Supprime une recommandation
     */
    @Transactional
    public boolean deleteRecommandation(Long id) {
        log.info("Suppression de la recommandation avec l'ID: {}", id);
        Optional<Recommandation> recommandationOpt = recommandationRepository.findById(id);
        if (recommandationOpt.isPresent()) {
            recommandationRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    /**
     * MÉTHODES DE CONVERSION
     */
    
    /**
     * Convertit une entité en DTO
     */
    private ConseilMedicalDTO convertToDTO(ConseilMedical conseil) {
        ConseilMedicalDTO dto = new ConseilMedicalDTO();
        dto.setId(conseil.getId());
        dto.setTitre(conseil.getTitre());
        dto.setContenu(conseil.getContenu());
        dto.setResume(conseil.getResume());
        dto.setMotsCles(conseil.getMotsCles());
        dto.setPathologieId(conseil.getPathologie().getId());
        dto.setPathologieNom(conseil.getPathologie().getNom());
        dto.setAuteurId(Long.valueOf(conseil.getAuteur().getId()));
        dto.setAuteurNom(conseil.getAuteur().getLastName() + " " + conseil.getAuteur().getFirstName());
        dto.setDateCreation(conseil.getDateCreation());
        dto.setDateModification(conseil.getDateModification());
        dto.setDatePublication(conseil.getDatePublication());
        dto.setStatut(conseil.getStatut().name());
        dto.setVisiblePublic(conseil.getVisiblePublic());
        
        if (conseil.getApprouvePar() != null) {
            dto.setApprouveParId(Long.valueOf(conseil.getApprouvePar().getId()));
            dto.setApprouveParNom(conseil.getApprouvePar().getLastName() + " " + 
                conseil.getApprouvePar().getFirstName());
        }
        
        // Convertir les sections
        if (conseil.getSections() != null) {
            dto.setSections(conseil.getSections().stream()
                    .map(this::convertSectionToInnerDTO)
                    .collect(Collectors.toList()));
        } else {
            dto.setSections(Collections.emptyList());
        }
        
        // Convertir les ressources
        if (conseil.getRessources() != null) {
            dto.setRessources(conseil.getRessources().stream()
                    .map(this::convertRessourceToInnerDTO)
                    .collect(Collectors.toList()));
        } else {
            dto.setRessources(Collections.emptyList());
        }
        
        // Convertir les recommandations
        if (conseil.getRecommandations() != null) {
            dto.setRecommandations(conseil.getRecommandations().stream()
                    .map(this::convertRecommandationToInnerDTO)
                    .collect(Collectors.toList()));
        } else {
            dto.setRecommandations(Collections.emptyList());
        }
        
        return dto;
    }
    
    /**
     * Convertit une entité SectionConseil en DTO interne pour le DTO ConseilMedical
     */
    private ConseilMedicalDTO.SectionConseilDTO convertSectionToInnerDTO(SectionConseil section) {
        return ConseilMedicalDTO.SectionConseilDTO.builder()
                .id(section.getId())
                .titre(section.getTitre())
                .contenu(section.getContenu())
                .ordre(section.getOrdre())
                .build();
    }
    
    /**
     * Convertit une entité RessourceConseil en DTO interne pour le DTO ConseilMedical
     */
    private ConseilMedicalDTO.RessourceConseilDTO convertRessourceToInnerDTO(RessourceConseil ressource) {
        return ConseilMedicalDTO.RessourceConseilDTO.builder()
                .id(ressource.getId())
                .titre(ressource.getTitre())
                .type(ressource.getType().name())
                .url(ressource.getUrl())
                .description(ressource.getDescription())
                .build();
    }
    
    /**
     * Convertit une entité Recommandation en DTO interne pour le DTO ConseilMedical
     */
    private ConseilMedicalDTO.RecommandationDTO convertRecommandationToInnerDTO(Recommandation recommandation) {
        return ConseilMedicalDTO.RecommandationDTO.builder()
                .id(recommandation.getId())
                .texte(recommandation.getTexte())
                .ordre(recommandation.getOrdre())
                .build();
    }
    
    /**
     * Convertit une entité SectionConseil en DTO
     */
    private SectionConseilDTO convertSectionToDTO(SectionConseil section) {
        return SectionConseilDTO.builder()
                .id(section.getId())
                .titre(section.getTitre())
                .contenu(section.getContenu())
                .ordre(section.getOrdre())
                .conseilId(section.getConseil().getId())
                .build();
    }
    
    /**
     * Convertit une entité RessourceConseil en DTO
     */
    private RessourceConseilDTO convertRessourceToDTO(RessourceConseil ressource) {
        return RessourceConseilDTO.builder()
                .id(ressource.getId())
                .titre(ressource.getTitre())
                .type(ressource.getType().name())
                .url(ressource.getUrl())
                .description(ressource.getDescription())
                .conseilId(ressource.getConseil().getId())
                .build();
    }
    
    /**
     * Convertit une entité Recommandation en DTO
     */
    private RecommandationDTO convertRecommandationToDTO(Recommandation recommandation) {
        return RecommandationDTO.builder()
                .id(recommandation.getId())
                .texte(recommandation.getTexte())
                .ordre(recommandation.getOrdre())
                .conseilId(recommandation.getConseil().getId())
                .build();
    }
}
