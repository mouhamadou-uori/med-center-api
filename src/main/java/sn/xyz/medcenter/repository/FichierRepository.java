package sn.xyz.medcenter.repository;

import sn.xyz.medcenter.model.Fichier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FichierRepository extends JpaRepository<Fichier, Integer> {
    List<Fichier> findByMessageId(Integer messageId);
}
