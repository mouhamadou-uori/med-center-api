package sn.xyz.medcenter.repository;

import sn.xyz.medcenter.model.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Integer> {
    List<Prescription> findByDateEmissionBetween(LocalDate start, LocalDate end);
}
