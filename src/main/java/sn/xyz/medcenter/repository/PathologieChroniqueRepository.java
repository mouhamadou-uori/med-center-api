package sn.xyz.medcenter.repository;

import sn.xyz.medcenter.model.PathologieChronique;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PathologieChroniqueRepository extends JpaRepository<PathologieChronique, Integer> {
    List<PathologieChronique> findByPatientId(Integer patientId);
    List<PathologieChronique> findByNomContainingIgnoreCase(String nom);
}
