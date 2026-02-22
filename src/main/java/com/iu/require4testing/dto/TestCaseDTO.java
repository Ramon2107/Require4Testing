package com.iu.require4testing.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * Data Transfer Object für Testfälle.
 * Wird für die API-Kommunikation verwendet.
 * Enthält Validierungsregeln gemäß Jakarta Bean Validation.
 *
 * @author Require4Testing Team
 * @version 1.3.0
 */
public class TestCaseDTO {

    private Long id;

    /** Fachliche ID des Testfalls (z.B. TC-2025-001). */
    private String readableId;

    @NotBlank(message = "Der Name darf nicht leer sein")
    @Size(min = 1, max = 500, message = "Der Name muss zwischen 1 und 500 Zeichen lang sein")
    private String name;

    @Size(max = 10000, message = "Die Beschreibung darf maximal 10000 Zeichen lang sein")
    private String description;

    @Size(max = 50000, message = "Die Testschritte dürfen maximal 50000 Zeichen lang sein")
    private String testSteps;

    @NotNull(message = "Die Anforderungs-ID muss angegeben werden")
    private Long requirementId;

    @NotNull(message = "Der Ersteller muss angegeben werden")
    private Long createdBy;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Standardkonstruktor für Frameworks.
     */
    public TestCaseDTO() {}

    /**
     * Konstruktor mit allen Feldern zur vollständigen Initialisierung.
     *
     * @param id Testfall-ID
     * @param readableId Fachliche ID
     * @param name Name des Testfalls
     * @param description Beschreibung
     * @param testSteps Testschritte
     * @param requirementId ID der zugehörigen Anforderung
     * @param createdBy ID des Erstellers
     * @param createdAt Erstellungszeitpunkt
     * @param updatedAt Aktualisierungszeitpunkt
     */
    public TestCaseDTO(Long id, String readableId, String name, String description, String testSteps,
                       Long requirementId, Long createdBy, LocalDateTime createdAt,
                       LocalDateTime updatedAt) {
        this.id = id;
        this.readableId = readableId;
        this.name = name;
        this.description = description;
        this.testSteps = testSteps;
        this.requirementId = requirementId;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // --- Getter und Setter ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /** @return Fachliche ID. */
    public String getReadableId() {
        return readableId;
    }

    /** @param readableId Fachliche ID. */
    public void setReadableId(String readableId) {
        this.readableId = readableId;
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