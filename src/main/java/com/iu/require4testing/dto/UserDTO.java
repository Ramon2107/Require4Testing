package com.iu.require4testing.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

/**
 * Data Transfer Object für Benutzer.
 * Wird für die API-Kommunikation verwendet.
 * Enthält Validierungsregeln gemäß Jakarta Bean Validation.
 */
public class UserDTO {
    
    private Long id;
    
    @NotBlank(message = "Der Benutzername darf nicht leer sein")
    @Size(min = 3, max = 100, message = "Der Benutzername muss zwischen 3 und 100 Zeichen lang sein")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", 
             message = "Der Benutzername darf nur Buchstaben, Zahlen und Unterstriche enthalten")
    private String username;
    
    @NotBlank(message = "Die E-Mail-Adresse darf nicht leer sein")
    @Email(message = "Bitte geben Sie eine gültige E-Mail-Adresse ein")
    private String email;
    
    @Size(min = 8, max = 100, message = "Das Passwort muss zwischen 8 und 100 Zeichen lang sein")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
             message = "Das Passwort muss mindestens einen Großbuchstaben, einen Kleinbuchstaben, eine Zahl und ein Sonderzeichen enthalten")
    private String password;
    
    @Pattern(regexp = "^(ADMIN|MANAGER|TESTER|USER)$", 
             message = "Rolle muss ADMIN, MANAGER, TESTER oder USER sein")
    private String role;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    /**
     * Standardkonstruktor.
     */
    public UserDTO() {}
    
    /**
     * Konstruktor mit allen Feldern.
     * 
     * @param id Benutzer-ID
     * @param username Benutzername
     * @param email E-Mail-Adresse
     * @param role Rolle des Benutzers
     * @param createdAt Erstellungszeitpunkt
     * @param updatedAt Aktualisierungszeitpunkt
     */
    public UserDTO(Long id, String username, String email, String role, 
                   LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // Getter und Setter
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
