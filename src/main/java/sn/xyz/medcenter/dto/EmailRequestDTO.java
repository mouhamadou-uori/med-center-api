package sn.xyz.medcenter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailRequestDTO {
    
    @NotEmpty(message = "La liste des destinataires ne peut pas être vide")
    private List<@Email(message = "Format d'email invalide") String> to;
    
    @NotBlank(message = "Le sujet ne peut pas être vide")
    private String subject;
    
    @NotBlank(message = "Le contenu ne peut pas être vide")
    private String html;
    
    private List<String> cc;
    private List<String> bcc;
    private String text; // Version texte de l'email (optionnel)
}
