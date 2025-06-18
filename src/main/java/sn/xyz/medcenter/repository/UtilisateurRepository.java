package sn.xyz.medcenter.repository;

import sn.xyz.medcenter.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Integer> {
    public Utilisateur findByUsername(String username);
}