package com.iu.require4testing.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

/**
 * Data Transfer Object für Anforderungen.
 * Wird für die API-Kommunikation verwendet.
 * Enthält Validierungsregeln gemäß Jakarta Bean Validation.
 */
public class RequirementDTO {
    
    private Long id;
    
    @NotBlank(message = "Der Name darf nicht leer sein")
    @Size(min = 1, max = 500, message = "Der Name muss zwischen 1 und 500 Zeichen lang sein")
    private String name;
    
    @Size(max = 10000, message = "Die Beschreibung darf maximal 10000 Zeichen lang sein")
    private String description;
    
    @NotNull(message = "Der Ersteller muss angegeben werden")
    private Long createdBy;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    /**
     * Standardkonstruktor.
     */
    public RequirementDTO() {}
    
    /**
     * Konstruktor mit allen Feldern.
     * 
     * @param id Anforderungs-ID
     * @param name Name der Anforderung
     * @param description Beschreibung
     * @param createdBy ID des Erstellers
     * @param createdAt Erstellungszeitpunkt
     * @param updatedAt Aktualisierungszeitpunkt
     */
    public RequirementDTO(Long id, String name, String description, Long createdBy, 
                          LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createdBy = createdBy;
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
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Long getCreatedBy() {
        return createdBy;
    }
    
    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
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
