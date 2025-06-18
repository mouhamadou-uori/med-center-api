package sn.xyz.medcenter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.web.bind.annotation.*;
import sn.xyz.medcenter.service.CustomUserDetailsService;
import sn.xyz.medcenter.dto.AuthRequest;
import sn.xyz.medcenter.dto.AuthResponse;
import sn.xyz.medcenter.util.JwtUtil;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
//@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", methods = {org.springframework.web.bind.annotation.RequestMethod.GET, org.springframework.web.bind.annotation.RequestMethod.POST, org.springframework.web.bind.annotation.RequestMethod.PUT, org.springframework.web.bind.annotation.RequestMethod.DELETE, org.springframework.web.bind.annotation.RequestMethod.OPTIONS}, allowCredentials = "true", maxAge = 3600)
public class AuthController {

    private final OAuth2AuthorizedClientService authorizedClientService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    public AuthController(OAuth2AuthorizedClientService authorizedClientService) {
        this.authorizedClientService = authorizedClientService;
    }

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Endpoint d'authentification stateless pour obtenir un JWT.
     * Ce endpoint ne crée aucune session côté serveur (stateless).
     *
     * Les informations de session/context sont inutiles ici car le JWT sera utilisé pour chaque requête suivante.
     */
    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthRequest authRequest) {
        try {
            // Authentifier l'utilisateur
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );

            // (Inutile en stateless) : SecurityContextHolder.getContext().setAuthentication(authentication);
            // On ne stocke rien côté serveur, le JWT suffit !

            // Charger les détails de l'utilisateur
            final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());

            // Générer le token JWT
            final String jwt = jwtUtil.generateToken(userDetails);

            // Récupérer les rôles de l'utilisateur
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            // Retourner la réponse d'authentification avec le token
            return ResponseEntity.ok(new AuthResponse(jwt, roles.toString()));

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Identifiants incorrects");
        }
    }

    @GetMapping("/test")
    public String login() {
        return "Hello darling";
    }

    @GetMapping("/")
    public String getUserInfo(Principal user, @AuthenticationPrincipal OidcUser oidcUser) {
        StringBuffer userInfo = new StringBuffer();
        if (user instanceof UsernamePasswordAuthenticationToken) {
            userInfo.append(getUsernamePasswordLoginInfo(user));
        } else if (user instanceof OAuth2AuthenticationToken) {
            userInfo.append(getOauth2LoginInfo(user, oidcUser));
        }
        return userInfo.toString();
    }

    private StringBuffer getUsernamePasswordLoginInfo(Principal user)
    {
        StringBuffer usernameInfo = new StringBuffer();

        UsernamePasswordAuthenticationToken token = ((UsernamePasswordAuthenticationToken) user);
        if(token.isAuthenticated()){
            User u = (User) token.getPrincipal();
            usernameInfo.append("Welcome, " + u.getUsername());
        }
        else{
            usernameInfo.append("NA");
        }
        return usernameInfo;
    }

    private StringBuffer getOauth2LoginInfo(Principal user, OidcUser oidcUser) {
		StringBuffer protectedInfo = new StringBuffer();

		OAuth2AuthenticationToken authToken = ((OAuth2AuthenticationToken) user);
		OAuth2AuthorizedClient authClient = this.authorizedClientService
				.loadAuthorizedClient(authToken.getAuthorizedClientRegistrationId(), authToken.getName());
		if (authToken.isAuthenticated()) {

			Map<String, Object> userAttributes = ((DefaultOAuth2User) authToken.getPrincipal()).getAttributes();

			String userToken = authClient.getAccessToken().getTokenValue();
			protectedInfo.append("Welcome, " + userAttributes.get("name") + "<br><br>");
			protectedInfo.append("e-mail: " + userAttributes.get("email") + "<br><br>");
			protectedInfo.append("Access Token: " + userToken + "<br><br>");

			if (oidcUser != null) {
				OidcIdToken idToken = oidcUser.getIdToken();
				if (idToken != null) {
					protectedInfo.append("idToken value: " + idToken.getTokenValue() + "<br><br>");
					protectedInfo.append("Token mapped values <br><br>");
					Map<String, Object> claims = idToken.getClaims();
					for (String key : claims.keySet()) {
						protectedInfo.append("  " + key + ": " + claims.get(key) + "<br>");
					}
				}
			}
		} else {
			protectedInfo.append("NA");
		}
		return protectedInfo;
	}

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        // Avec JWT, le logout côté serveur consiste simplement à effacer le contexte de sécurité
        SecurityContextHolder.clearContext();
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Déconnexion réussie");
        
        return ResponseEntity.ok(response);
    }
}
