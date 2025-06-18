package sn.xyz.medcenter.repository;

import sn.xyz.medcenter.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    List<Message> findByExpediteurId(Integer expediteurId);
    List<Message> findByDestinataireId(Integer destinataireId);
    List<Message> findByDestinataireIdAndLuFalse(Integer destinataireId);
}
