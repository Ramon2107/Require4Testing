package com.iu.require4testing.dto;

import java.time.LocalDateTime;

/**
 * Data Transfer Object für Testfälle.
 * Wird für die API-Kommunikation verwendet.
 */
public class TestCaseDTO {
    
    private Long id;
    private String name;
    private String description;
    private String testSteps;
    private Long requirementId;
    private Long createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    /**
     * Standardkonstruktor.
     */
    public TestCaseDTO() {}
    
    /**
     * Konstruktor mit allen Feldern.
     * 
     * @param id Testfall-ID
     * @param name Name des Testfalls
     * @param description Beschreibung
     * @param testSteps Testschritte
     * @param requirementId ID der zugehörigen Anforderung
     * @param createdBy ID des Erstellers
     * @param createdAt Erstellungszeitpunkt
     * @param updatedAt Aktualisierungszeitpunkt
     */
    public TestCaseDTO(Long id, String name, String description, String testSteps, 
                       Long requirementId, Long createdBy, LocalDateTime createdAt, 
                       LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.testSteps = testSteps;
        this.requirementId = requirementId;
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
    
    public String getTestSteps() {
        return testSteps;
    }
    
    public void setTestSteps(String testSteps) {
        this.testSteps = testSteps;
    }
    
    public Long getRequirementId() {
        return requirementId;
    }
    
    public void setRequirementId(Long requirementId) {
        this.requirementId = requirementId;
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
