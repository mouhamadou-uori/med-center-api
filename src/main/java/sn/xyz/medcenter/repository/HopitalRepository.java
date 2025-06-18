package sn.xyz.medcenter.repository;

import sn.xyz.medcenter.model.Hopital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HopitalRepository extends JpaRepository<Hopital, Integer> {
    List<Hopital> findByRegion(String region);
    List<Hopital> findByType(String type);
}

