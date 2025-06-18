package sn.xyz.medcenter.repository;

import sn.xyz.medcenter.model.ProfessionnelSante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfessionnelSanteRepository extends JpaRepository<ProfessionnelSante, Integer> {
    List<ProfessionnelSante> findBySpecialite(String specialite);
    List<ProfessionnelSante> findByHopitalId(Integer hopitalId);
    Optional<ProfessionnelSante> findByNumeroOrdre(String numeroOrdre);
}
