package sn.xyz.medcenter.repository;

import sn.xyz.medcenter.model.DonneeSante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DonneeSanteRepository extends JpaRepository<DonneeSante, Integer> {
    List<DonneeSante> findByParametreId(Integer parametreId);
    List<DonneeSante> findByDateEnregistrementBetween(LocalDateTime start, LocalDateTime end);
    List<DonneeSante> findByValideFalse();
}

