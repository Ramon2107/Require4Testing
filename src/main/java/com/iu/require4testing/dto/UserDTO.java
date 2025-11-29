package com.iu.require4testing.dto;

import java.time.LocalDateTime;

/**
 * Data Transfer Object für Benutzer.
 * Wird für die API-Kommunikation verwendet.
 */
public class UserDTO {
    
    private Long id;
    private String username;
    private String email;
    private String password;
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
