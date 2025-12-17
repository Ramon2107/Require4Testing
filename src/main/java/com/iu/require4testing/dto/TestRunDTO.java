package com.iu.require4testing.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * Data Transfer Object für Testläufe.
 */
public class TestRunDTO {

    private Long id;

    /** Sprechende ID, z.B. TR-2025-0001. */
    private String readableId;

    @NotBlank(message = "Der Name darf nicht leer sein")
    @Size(min = 1, max = 500, message = "Der Name muss zwischen 1 und 500 Zeichen lang sein")
    private String name;

    @Size(max = 10000, message = "Die Beschreibung darf maximal 10000 Zeichen lang sein")
    private String description;

    @Pattern(regexp = "^(PENDING|IN_PROGRESS|COMPLETED|CANCELLED|PLANNING|SUCCESSFUL|FAILED)$",
            message = "Status muss gültig sein")
    private String status;

    @NotNull(message = "Der Ersteller muss angegeben werden")
    private Long createdBy;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /** ID der verknüpften Anforderung. */
    private Long requirementId;

    public TestRunDTO() {}

    // Getter und Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getReadableId() { return readableId; }
    public void setReadableId(String readableId) { this.readableId = readableId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Long getCreatedBy() { return createdBy; }
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public Long getRequirementId() { return requirementId; }
    public void setRequirementId(Long requirementId) { this.requirementId = requirementId; }
}