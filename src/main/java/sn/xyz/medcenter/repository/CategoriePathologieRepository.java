package sn.xyz.medcenter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sn.xyz.medcenter.model.CategoriePathologie;

import java.util.List;

@Repository
public interface CategoriePathologieRepository extends JpaRepository<CategoriePathologie, Long> {
    List<CategoriePathologie> findByActiveTrue();
    List<CategoriePathologie> findByActiveTrueOrderByOrdre();
}
