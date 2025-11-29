package com.iu.require4testing.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Sicherheitskonfiguration für die Anwendung.
 * Konfiguriert Spring Security für REST-API-Zugriff.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    /**
     * Konfiguriert die Sicherheitsfilter-Kette.
     * Erlaubt Zugriff auf die H2-Konsole und API-Endpunkte.
     * 
     * CSRF-Schutz ist deaktiviert, da diese Anwendung eine REST-API bereitstellt,
     * die von API-Clients (nicht Browser-basierte Sessions) genutzt wird.
     * Bei Produktiveinsatz sollte ein Token-basiertes Authentifizierungssystem
     * (z.B. JWT) implementiert werden.
     * 
     * @param http Das HttpSecurity-Objekt
     * @return Die konfigurierte SecurityFilterChain
     * @throws Exception bei Konfigurationsfehlern
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // CSRF ist für stateless REST-APIs nicht erforderlich
            // Bei Browser-basierter Nutzung sollte CSRF aktiviert werden
            .csrf(csrf -> csrf.disable())
            .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers("/api/**").permitAll()
                .anyRequest().permitAll()
            );
        
        return http.build();
    }
    
    /**
     * Erstellt einen BCrypt-Passwort-Encoder für sichere Passwortspeicherung.
     * 
     * @return Der konfigurierte PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
