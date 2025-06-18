package sn.xyz.medcenter.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHashGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "passer";
        String encodedPassword = encoder.encode(rawPassword);
        
        System.out.println("Mot de passe en clair : " + rawPassword);
        System.out.println("Mot de passe hashé : " + encodedPassword);
        
        // Vérification
        boolean matches = encoder.matches(rawPassword, encodedPassword);
        System.out.println("Vérification : " + matches);
    }
}
