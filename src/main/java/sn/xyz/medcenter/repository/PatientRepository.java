package sn.xyz.medcenter.repository;

import sn.xyz.medcenter.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Integer> {
    Optional<Patient> findByNumeroSecu(String numeroSecu);
    
    @Query("SELECT DISTINCT c.patient FROM Consultation c WHERE c.professionnel.id = :professionnelId")
    List<Patient> findPatientsByProfessionnelId(@Param("professionnelId") Integer professionnelId);
}
