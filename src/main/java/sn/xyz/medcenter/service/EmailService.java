package sn.xyz.medcenter.service;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sn.xyz.medcenter.dto.EmailRequestDTO;
import sn.xyz.medcenter.dto.EmailResponseDTO;
import sn.xyz.medcenter.model.EmailLog;
import sn.xyz.medcenter.repository.EmailLogRepository;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    
    private final Resend resendClient;
    private final EmailLogRepository emailLogRepository;
    private final Executor emailExecutor = Executors.newCachedThreadPool();
    
    @Value("${resend.from.email}")
    private String fromEmail;
    
    /**
     * Envoie un email de manière synchrone
     */
    public EmailResponseDTO sendEmail(EmailRequestDTO emailRequest) {
        String recipientEmail = emailRequest.getTo().get(0); // Premier destinataire pour le log
        
        // Créer le log d'email avec le statut PENDING
        EmailLog emailLog = EmailLog.builder()
                .toEmail(recipientEmail)
                .fromEmail(fromEmail)
                .subject(emailRequest.getSubject())
                .status(EmailLog.EmailStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();
        
        try {
            log.info("Envoi d'email vers : {}", emailRequest.getTo());
            
            CreateEmailOptions.Builder emailBuilder = CreateEmailOptions.builder()
                    .from(fromEmail)
                    .to(emailRequest.getTo().toArray(new String[0]))
                    .subject(emailRequest.getSubject())
                    .html(emailRequest.getHtml());
            
            // Ajout des champs optionnels
            if (emailRequest.getText() != null && !emailRequest.getText().trim().isEmpty()) {
                emailBuilder.text(emailRequest.getText());
            }
            
            if (emailRequest.getCc() != null && !emailRequest.getCc().isEmpty()) {
                emailBuilder.cc(emailRequest.getCc().toArray(new String[0]));
            }
            
            if (emailRequest.getBcc() != null && !emailRequest.getBcc().isEmpty()) {
                emailBuilder.bcc(emailRequest.getBcc().toArray(new String[0]));
            }
            
            CreateEmailOptions emailOptions = emailBuilder.build();
            CreateEmailResponse response = resendClient.emails().send(emailOptions);
            
            // Mettre à jour le log avec le succès
            emailLog.setResendId(response.getId());
            emailLog.setStatus(EmailLog.EmailStatus.SUCCESS);
            emailLog.setSentAt(LocalDateTime.now());
            emailLogRepository.save(emailLog);
            
            log.info("Email envoyé avec succès. ID: {}", response.getId());
            return EmailResponseDTO.success(response.getId());
            
        } catch (ResendException e) {
            // Mettre à jour le log avec l'erreur
            emailLog.setStatus(EmailLog.EmailStatus.ERROR);
            emailLog.setErrorMessage(e.getMessage());
            emailLogRepository.save(emailLog);
            
            log.error("Erreur lors de l'envoi de l'email : {}", e.getMessage(), e);
            return EmailResponseDTO.error("Erreur lors de l'envoi : " + e.getMessage());
        } catch (Exception e) {
            // Mettre à jour le log avec l'erreur
            emailLog.setStatus(EmailLog.EmailStatus.ERROR);
            emailLog.setErrorMessage(e.getMessage());
            emailLogRepository.save(emailLog);
            
            log.error("Erreur inattendue lors de l'envoi de l'email : {}", e.getMessage(), e);
            return EmailResponseDTO.error("Erreur inattendue : " + e.getMessage());
        }
    }
    
    /**
     * Envoie un email de manière asynchrone
     */
    public CompletableFuture<EmailResponseDTO> sendEmailAsync(EmailRequestDTO emailRequest) {
        return CompletableFuture.supplyAsync(() -> sendEmail(emailRequest), emailExecutor);
    }
    
    /**
     * Méthode utilitaire pour envoyer un email simple
     */
    public EmailResponseDTO sendSimpleEmail(String to, String subject, String htmlContent) {
        EmailRequestDTO emailRequest = EmailRequestDTO.builder()
                .to(java.util.List.of(to))
                .subject(subject)
                .html(htmlContent)
                .build();
        
        return sendEmail(emailRequest);
    }
    
    /**
     * Méthode pour envoyer un email de bienvenue
     */
    public EmailResponseDTO sendWelcomeEmail(String to, String firstName) {
        String subject = "Bienvenue sur MedCenter";
        String htmlContent = String.format("""
            <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;">
                <h2 style="color: #2c5aa0;">Bienvenue sur MedCenter, %s !</h2>
                <p>Nous sommes ravis de vous accueillir sur notre plateforme médicale.</p>
                <p>Votre compte a été créé avec succès. Vous pouvez maintenant accéder à nos services.</p>
                <div style="margin: 30px 0; padding: 20px; background-color: #f8f9fa; border-radius: 5px;">
                    <h3 style="color: #2c5aa0; margin-top: 0;">Que pouvez-vous faire ?</h3>
                    <ul>
                        <li>Consulter des conseils médicaux</li>
                        <li>Prendre des rendez-vous</li>
                        <li>Gérer votre dossier médical</li>
                        <li>Communiquer avec des professionnels de santé</li>
                    </ul>
                </div>
                <p>Si vous avez des questions, n'hésitez pas à nous contacter.</p>
                <p style="color: #666;">Cordialement,<br>L'équipe MedCenter</p>
            </div>
            """, firstName);
        
        return sendSimpleEmail(to, subject, htmlContent);
    }
    
    /**
     * Méthode pour envoyer un email de réinitialisation de mot de passe
     */
    public EmailResponseDTO sendPasswordResetEmail(String to, String resetToken) {
        String subject = "Réinitialisation de votre mot de passe - MedCenter";
        String resetUrl = "http://localhost:9000/reset-password?token=" + resetToken;
        
        String htmlContent = String.format("""
            <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;">
                <h2 style="color: #2c5aa0;">Réinitialisation de mot de passe</h2>
                <p>Vous avez demandé la réinitialisation de votre mot de passe pour votre compte MedCenter.</p>
                <p>Cliquez sur le bouton ci-dessous pour créer un nouveau mot de passe :</p>
                <div style="text-align: center; margin: 30px 0;">
                    <a href="%s" style="background-color: #2c5aa0; color: white; padding: 12px 30px; text-decoration: none; border-radius: 5px; display: inline-block;">
                        Réinitialiser mon mot de passe
                    </a>
                </div>
                <p style="color: #666; font-size: 14px;">Si vous n'avez pas demandé cette réinitialisation, vous pouvez ignorer cet email.</p>
                <p style="color: #666; font-size: 14px;">Ce lien expire dans 24 heures.</p>
                <p style="color: #666;">Cordialement,<br>L'équipe MedCenter</p>
            </div>
            """, resetUrl);
        
        return sendSimpleEmail(to, subject, htmlContent);
    }
}
