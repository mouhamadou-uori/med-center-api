package sn.xyz.medcenter.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "admin123"; // Remplacez par le mot de passe que vous voulez hasher
        String encodedPassword = encoder.encode(rawPassword);
        System.out.println("Mot de passe hashé : " + encodedPassword);
    }
}
