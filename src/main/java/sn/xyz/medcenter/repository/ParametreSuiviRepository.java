package sn.xyz.medcenter.repository;

import sn.xyz.medcenter.model.ParametreSuivi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParametreSuiviRepository extends JpaRepository<ParametreSuivi, Integer> {
    List<ParametreSuivi> findByPathologieId(Integer pathologieId);
    List<ParametreSuivi> findByAlerteActiveTrue();
}
