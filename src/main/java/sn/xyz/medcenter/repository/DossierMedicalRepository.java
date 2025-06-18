package sn.xyz.medcenter.repository;

import sn.xyz.medcenter.model.DossierMedical;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DossierMedicalRepository extends JpaRepository<DossierMedical, Integer> {
    Optional<DossierMedical> findByPatientId(Integer patientId);
}
