package com.iu.require4testing.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

/**
 * Data Transfer Object für Testergebnisse.
 * Wird für die API-Kommunikation verwendet.
 * Enthält Validierungsregeln gemäß Jakarta Bean Validation.
 */
public class TestResultDTO {
    
    private Long id;
    
    @NotNull(message = "Die Testfall-ID muss angegeben werden")
    private Long testCaseId;
    
    @NotNull(message = "Die Testlauf-ID muss angegeben werden")
    private Long testRunId;
    
    @NotNull(message = "Die Tester-ID muss angegeben werden")
    private Long testerId;
    
    @NotNull(message = "Der Status muss angegeben werden")
    @Pattern(regexp = "^(PASSED|FAILED|BLOCKED|SKIPPED|NOT_EXECUTED)$", 
             message = "Status muss PASSED, FAILED, BLOCKED, SKIPPED oder NOT_EXECUTED sein")
    private String status;
    
    @Size(max = 10000, message = "Die Notizen dürfen maximal 10000 Zeichen lang sein")
    private String notes;
    
    private LocalDateTime executedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    /**
     * Standardkonstruktor.
     */
    public TestResultDTO() {}
    
    /**
     * Konstruktor mit allen Feldern.
     * 
     * @param id Testergebnis-ID
     * @param testCaseId ID des Testfalls
     * @param testRunId ID des Testlaufs
     * @param testerId ID des Testers
     * @param status Status des Ergebnisses
     * @param notes Notizen
     * @param executedAt Ausführungszeitpunkt
     * @param createdAt Erstellungszeitpunkt
     * @param updatedAt Aktualisierungszeitpunkt
     */
    public TestResultDTO(Long id, Long testCaseId, Long testRunId, Long testerId, 
                         String status, String notes, LocalDateTime executedAt, 
                         LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.testCaseId = testCaseId;
        this.testRunId = testRunId;
        this.testerId = testerId;
        this.status = status;
        this.notes = notes;
        this.executedAt = executedAt;
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
    
    public Long getTestCaseId() {
        return testCaseId;
    }
    
    public void setTestCaseId(Long testCaseId) {
        this.testCaseId = testCaseId;
    }
    
    public Long getTestRunId() {
        return testRunId;
    }
    
    public void setTestRunId(Long testRunId) {
        this.testRunId = testRunId;
    }
    
    public Long getTesterId() {
        return testerId;
    }
    
    public void setTesterId(Long testerId) {
        this.testerId = testerId;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public LocalDateTime getExecutedAt() {
        return executedAt;
    }
    
    public void setExecutedAt(LocalDateTime executedAt) {
        this.executedAt = executedAt;
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
