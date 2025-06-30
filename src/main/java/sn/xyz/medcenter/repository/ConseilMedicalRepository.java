package sn.xyz.medcenter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sn.xyz.medcenter.model.ConseilMedical;

import java.util.List;

@Repository
public interface ConseilMedicalRepository extends JpaRepository<ConseilMedical, Long> {
    List<ConseilMedical> findByPathologieId(Long pathologieId);
    List<ConseilMedical> findByStatut(ConseilMedical.StatutConseil statut);
    List<ConseilMedical> findByVisiblePublicTrue();
    List<ConseilMedical> findByVisiblePublicTrueAndStatut(ConseilMedical.StatutConseil statut);
    List<ConseilMedical> findByAuteurId(Long auteurId);
}
