package sn.xyz.medcenter.repository;

import sn.xyz.medcenter.model.Consultation;
import sn.xyz.medcenter.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface ConsultationRepository extends JpaRepository<Consultation, Integer> {
    List<Consultation> findByPatientId(Integer patientId);
    List<Consultation> findByProfessionnelId(Integer professionnelId);
    List<Consultation> findByDateHeureBetween(LocalDateTime start, LocalDateTime end);

    long countByProfessionnelId(Integer professionnelId);

    @Query("SELECT COUNT(DISTINCT c.patient.id) FROM Consultation c WHERE c.professionnel.id = :professionnelId")
    long countDistinctPatientsByProfessionnelId(@Param("professionnelId") Integer professionnelId);

    @Query("SELECT DISTINCT c.patient FROM Consultation c WHERE c.professionnel.id = :professionnelId")
    List<Patient> findDistinctPatientsByProfessionnelId(@Param("professionnelId") Integer professionnelId);
}
