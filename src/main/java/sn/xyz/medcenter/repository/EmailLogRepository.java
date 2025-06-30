package sn.xyz.medcenter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.xyz.medcenter.model.EmailLog;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EmailLogRepository extends JpaRepository<EmailLog, Long> {
    
    List<EmailLog> findByToEmailOrderByCreatedAtDesc(String toEmail);
    
    List<EmailLog> findByFromEmailOrderByCreatedAtDesc(String fromEmail);
    
    @Query("SELECT e FROM EmailLog e WHERE (e.toEmail = :email OR e.fromEmail = :email) ORDER BY e.createdAt DESC")
    List<EmailLog> findEmailsByUserEmail(@Param("email") String email);
    
    @Query("SELECT e FROM EmailLog e WHERE (e.toEmail = :email OR e.fromEmail = :email) " +
           "AND e.createdAt >= :startDate ORDER BY e.createdAt DESC")
    List<EmailLog> findEmailsByUserEmailSince(@Param("email") String email, @Param("startDate") LocalDateTime startDate);
    
    List<EmailLog> findByStatusOrderByCreatedAtDesc(EmailLog.EmailStatus status);
}
