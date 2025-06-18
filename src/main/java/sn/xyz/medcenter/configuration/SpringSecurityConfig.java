package sn.xyz.medcenter.configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//import sn.xyz.medcenter.filter.JwtRequestFilter;
import sn.xyz.medcenter.service.CustomUserDetailsService;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true) // met la sécurité au niveau des méthodes avec les annotations comme @PreAuthorize

/* configuration de base juste pour tester avec un formulaire de login genere par spring security
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http.formLogin(Customizer.withDefaults()).build();
} */

public class SpringSecurityConfig {

    @Autowired
    CustomUserDetailsService customUserDetailsService;
    
//    @Autowired
//    private JwtRequestFilter jwtRequestFilter;
    
//    @Autowired
//    private CorsConfigurationSource corsConfigurationSource;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            // Désactive la gestion de session côté serveur (stateless)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // Désactive CSRF car inutile pour une API stateless (JWT)
            .csrf(csrf -> csrf.disable())

            // Active la configuration CORS
            .cors(Customizer.withDefaults())

            // Autorise le endpoint de login sans authentification
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/login").permitAll()
                .requestMatchers("/api/auth/test").hasRole("ADMIN")
                // Ajoute ici d'autres endpoints publics si besoin
                .anyRequest().authenticated()
            )
            // Active la validation JWT côté resource server avec conversion des authorities
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
            )
            /*
             * .formLogin() et .oauth2Login() sont inutiles pour une API REST stateless avec JWT,
             * car l'authentification se fait via le endpoint /api/auth/login et le token.
             * On les laisse commentés pour référence si tu veux tester un jour une auth web classique.
             */
            //.formLogin(Customizer.withDefaults())
            //.oauth2Login(Customizer.withDefaults())
            .build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Convertit le claim 'authorities' du JWT en rôles Spring Security.
     * Nécessaire pour que .hasRole('ADMIN') fonctionne avec les tokens générés.
     */
    private org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter jwtAuthenticationConverter() {
        org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter converter =
            new org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            Object authoritiesClaim = jwt.getClaim("authorities");
            if (authoritiesClaim instanceof java.util.Collection<?>) {
                java.util.Collection<?> authorities = (java.util.Collection<?>) authoritiesClaim;
                return authorities.stream()
                        .map(Object::toString)
                        .map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role)
                        .map(org.springframework.security.core.authority.SimpleGrantedAuthority::new)
                        .collect(java.util.stream.Collectors.toList());
            }
            return java.util.Collections.emptyList();
        });
        return converter;
    }

//    @Bean
//    public AuthenticationManager authenticationManager() {
//        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//        provider.setUserDetailsService(customUserDetailsService);
//        provider.setPasswordEncoder(passwordEncoder());
//        return new ProviderManager(provider);
//    }
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(customUserDetailsService).passwordEncoder(bCryptPasswordEncoder);
        return authenticationManagerBuilder.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Collections.singletonList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}