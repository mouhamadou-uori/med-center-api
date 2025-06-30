package sn.xyz.medcenter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.xyz.medcenter.dto.EmailResponseDTO;
import sn.xyz.medcenter.service.EmailService;

@RestController
@RequestMapping("/api/test")
public class EmailTestController {
    
    @Autowired
    private EmailService emailService;
    
    @PostMapping("/send-test-email")
    public ResponseEntity<EmailResponseDTO> sendTestEmail(@RequestParam String to) {
        EmailResponseDTO response = emailService.sendSimpleEmail(
            to,
            "Test Email - MedCenter",
            "<h1>Test Email</h1><p>Ceci est un email de test depuis MedCenter!</p>"
        );
        
        if ("SUCCESS".equals(response.getStatus())) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(500).body(response);
        }
    }
}
