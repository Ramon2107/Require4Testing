package com.iu.require4testing.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Zentrale Sicherheitskonfiguration für die Spring Boot Anwendung.
 * <p>
 * Diese Klasse konfiguriert die {@link SecurityFilterChain} und definiert die
 * Sicherheitsregeln für HTTP-Anfragen. Sie stellt zudem Beans für die Passwort-Verschlüsselung bereit.
 * </p>
 * <p>
 * <strong>Wichtiger Hinweis:</strong> Da die Anwendung in der aktuellen Ausbaustufe eine eigene,
 * Session-basierte Login-Logik im {@code UiController} implementiert, werden hier die standardmäßigen
 * Sicherheitsmechanismen von Spring Security (wie Form-Login oder CSRF-Schutz) weitgehend deaktiviert
 * oder gelockert, um Konflikte zu vermeiden. Die Autorisierung erfolgt auf Controller-Ebene.
 * </p>
 *
 * @author Require4Testing Team
 * @version 1.0.0
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Konfiguriert die Filterkette für die Web-Sicherheit.
     * Hier werden folgende Einstellungen vorgenommen:
     * <ul>
     *     <li>CSRF-Schutz wird deaktiviert (für vereinfachte Entwicklung/Testen).</li>
     *     <li>Frame-Optionen werden deaktiviert (notwendig für die H2-Datenbank-Konsole).</li>
     *     <li>Alle HTTP-Requests werden zugelassen ({@code permitAll}), da die Prüfung im Controller erfolgt.</li>
     *     <li>Standard-Login und -Logout von Spring Security werden deaktiviert.</li>
     * </ul>
     *
     * @param http Das {@link HttpSecurity} Objekt zur Konfiguration.
     * @return Die fertig konfigurierte {@link SecurityFilterChain}.
     * @throws Exception Falls bei der Konfiguration ein Fehler auftritt.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/**").permitAll()
                )
                .formLogin(form -> form.disable())
                .logout(logout -> logout.disable());

        return http.build();
    }

    /**
     * Stellt einen Encoder für die Passwort-Verschlüsselung bereit.
     * <p>
     * Verwendet den BCrypt-Algorithmus, der als Industriestandard für sicheres Hashing von Passwörtern gilt.
     * </p>
     *
     * @return Eine Instanz von {@link BCryptPasswordEncoder}.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}