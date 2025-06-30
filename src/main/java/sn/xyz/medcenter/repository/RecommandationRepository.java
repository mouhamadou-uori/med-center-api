package sn.xyz.medcenter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sn.xyz.medcenter.model.Recommandation;

import java.util.List;

@Repository
public interface RecommandationRepository extends JpaRepository<Recommandation, Long> {
    List<Recommandation> findByConseilIdOrderByOrdre(Long conseilId);
}
