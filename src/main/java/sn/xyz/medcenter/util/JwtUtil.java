package sn.xyz.medcenter.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    // Clé privée RSA pour signer le JWT (RS256)
    // Le fichier private.pem doit être dans src/main/resources
    private final Key privateKey;

    // Durée de validité du token (10 heures)
    private final long jwtExpirationInMs = 10 * 60 * 60 * 1000;

    public JwtUtil() {
        this.privateKey = loadPrivateKey();
    }

    // Chargement de la clé privée RSA depuis le fichier PEM
    private Key loadPrivateKey() {
        try (java.io.InputStream is = getClass().getClassLoader().getResourceAsStream("private.pem")) {
            String privateKeyPem = new String(is.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8)
                .replaceAll("-----BEGIN (.*)-----", "")
                .replaceAll("-----END (.*)-----", "")
                .replaceAll("\\s", "");
            byte[] pkcs8EncodedBytes = java.util.Base64.getDecoder().decode(privateKeyPem);
            java.security.spec.PKCS8EncodedKeySpec keySpec = new java.security.spec.PKCS8EncodedKeySpec(pkcs8EncodedBytes);
            java.security.KeyFactory kf = java.security.KeyFactory.getInstance("RSA");
            return kf.generatePrivate(keySpec);
        } catch (Exception e) {
            throw new RuntimeException("Impossible de charger la clé privée RSA pour JWT", e);
        }
    }

    // Extraire le nom d'utilisateur du token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extraire la date d'expiration du token
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Extraire une information spécifique du token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Extraire toutes les informations du token
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(privateKey).build().parseClaimsJws(token).getBody();
    }

    // Vérifier si le token est expiré
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Extraire les rôles du token
    public String extractRoles(String token) {
        Claims claims = extractAllClaims(token);
        String roles = claims.get("authorities").toString();
        return roles;
    }

    // Générer un token pour l'utilisateur
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        // Ajouter les rôles/autorités de l'utilisateur dans le token
        List<String> authorities = userDetails.getAuthorities().stream()
                .map(authority -> authority.getAuthority())
                .collect(Collectors.toList());
        claims.put("authorities", authorities);
        return createToken(claims, userDetails.getUsername());
    }

    // Créer le token avec les paramètres spécifiés (RS256)
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    // Valider le token pour un utilisateur donné
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
