package sn.xyz.medcenter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sn.xyz.medcenter.model.SectionConseil;

import java.util.List;

@Repository
public interface SectionConseilRepository extends JpaRepository<SectionConseil, Long> {
    List<SectionConseil> findByConseilIdOrderByOrdre(Long conseilId);
}
