package sn.xyz.medcenter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sn.xyz.medcenter.model.RessourceConseil;

import java.util.List;

@Repository
public interface RessourceConseilRepository extends JpaRepository<RessourceConseil, Long> {
    List<RessourceConseil> findByConseilId(Long conseilId);
}
