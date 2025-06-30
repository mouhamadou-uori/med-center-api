package sn.xyz.medcenter.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sn.xyz.medcenter.dto.EmailRequestDTO;
import sn.xyz.medcenter.dto.EmailResponseDTO;
import sn.xyz.medcenter.service.EmailService;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/emails")
@RequiredArgsConstructor
@Slf4j
public class EmailController {
    
    private final EmailService emailService;
    
    @PostMapping("/send")
    // @PreAuthorize("hasRole('ADMIN') or hasRole('PROFESSIONNEL_SANTE')")
    public ResponseEntity<EmailResponseDTO> sendEmail(@Valid @RequestBody EmailRequestDTO emailRequest) {
        log.info("Demande d'envoi d'email reçue pour : {}", emailRequest.getTo());
        
        EmailResponseDTO response = emailService.sendEmail(emailRequest);
        
        if ("SUCCESS".equals(response.getStatus())) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    @PostMapping("/send-async")
    // @PreAuthorize("hasRole('ADMIN') or hasRole('PROFESSIONNEL_SANTE')")
    public ResponseEntity<String> sendEmailAsync(@Valid @RequestBody EmailRequestDTO emailRequest) {
        log.info("Demande d'envoi d'email asynchrone reçue pour : {}", emailRequest.getTo());
        
        CompletableFuture<EmailResponseDTO> futureResponse = emailService.sendEmailAsync(emailRequest);
        
        // Log du résultat de manière asynchrone
        futureResponse.thenAccept(response -> {
            if ("SUCCESS".equals(response.getStatus())) {
                log.info("Email asynchrone envoyé avec succès. ID: {}", response.getId());
            } else {
                log.error("Erreur lors de l'envoi asynchrone : {}", response.getMessage());
            }
        });
        
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body("Email mis en file d'attente pour envoi");
    }
    
    @PostMapping("/send-simple")
    // @PreAuthorize("hasRole('ADMIN') or hasRole('PROFESSIONNEL_SANTE')")
    public ResponseEntity<EmailResponseDTO> sendSimpleEmail(
            @RequestParam String to,
            @RequestParam String subject,
            @RequestParam String htmlContent) {
        
        log.info("Demande d'envoi d'email simple pour : {}", to);
        
        EmailResponseDTO response = emailService.sendSimpleEmail(to, subject, htmlContent);
        
        if ("SUCCESS".equals(response.getStatus())) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    @PostMapping("/send-welcome")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmailResponseDTO> sendWelcomeEmail(
            @RequestParam String to,
            @RequestParam String firstName) {
        
        log.info("Demande d'envoi d'email de bienvenue pour : {}", to);
        
        EmailResponseDTO response = emailService.sendWelcomeEmail(to, firstName);
        
        if ("SUCCESS".equals(response.getStatus())) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    @PostMapping("/send-password-reset")
    public ResponseEntity<EmailResponseDTO> sendPasswordResetEmail(
            @RequestParam String to,
            @RequestParam String resetToken) {
        
        log.info("Demande d'envoi d'email de réinitialisation pour : {}", to);
        
        EmailResponseDTO response = emailService.sendPasswordResetEmail(to, resetToken);
        
        if ("SUCCESS".equals(response.getStatus())) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
