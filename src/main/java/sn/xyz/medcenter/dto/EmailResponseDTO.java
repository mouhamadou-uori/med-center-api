package sn.xyz.medcenter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailResponseDTO {
    
    private String id; // ID de l'email retourné par Resend
    private String status;
    private String message;
    private LocalDateTime sentAt;
    
    public static EmailResponseDTO success(String emailId) {
        return EmailResponseDTO.builder()
                .id(emailId)
                .status("SUCCESS")
                .message("Email envoyé avec succès")
                .sentAt(LocalDateTime.now())
                .build();
    }
    
    public static EmailResponseDTO error(String errorMessage) {
        return EmailResponseDTO.builder()
                .status("ERROR")
                .message(errorMessage)
                .sentAt(LocalDateTime.now())
                .build();
    }
}
