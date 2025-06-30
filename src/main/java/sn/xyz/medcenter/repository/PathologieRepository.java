package sn.xyz.medcenter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sn.xyz.medcenter.model.Pathologie;

import java.util.List;
import java.util.Optional;

@Repository
public interface PathologieRepository extends JpaRepository<Pathologie, Long> {
    List<Pathologie> findByCategorieId(Long categorieId);
    List<Pathologie> findByCategorieIdAndPublieeTrue(Long categorieId);
    Optional<Pathologie> findBySlug(String slug);
    List<Pathologie> findByPublieeTrue();
    List<Pathologie> findByPublieeTrueOrderByOrdre();
}
