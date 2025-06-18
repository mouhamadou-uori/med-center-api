package sn.xyz.medcenter.repository;

import sn.xyz.medcenter.model.ServeurDICOM;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServeurDICOMRepository extends JpaRepository<ServeurDICOM, Integer> {
    List<ServeurDICOM> findByHopitalId(Integer hopitalId);
}
