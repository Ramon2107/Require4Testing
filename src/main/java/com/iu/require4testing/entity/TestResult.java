package com.iu.require4testing.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity-Klasse für Testergebnisse.
 *
 * <p>
 * Repräsentiert das Ergebnis einer konkreten Ausführung eines Testfalls in einem bestimmten Testlauf
 * (wer hat wann mit welchem Status ausgeführt und welche Notizen gibt es?).
 * Diese Entity hält ausschließlich den historischen/chronologischen Verlauf fest und ändert keine
 * fachlichen Zustände an Anforderung/Testlauf – sie dient der Nachvollziehbarkeit.
 * </p>
 *
 * <p>
 * Technische Hinweise:
 * </p>
 * <ul>
 *   <li>Zeitstempel für Erstellung/Aktualisierung werden per Lifecycle-Callbacks gepflegt.</li>
 *   <li>Alle Bezeichner sind einfache Referenzen (IDs) und keine JPA-Relations, um die Entkopplung
 *       bewusst schlank zu halten.</li>
 * </ul>
 *
 * @author Require4Testing Team
 * @version 1.4.0
 */
@Entity
@Table(name = "test_results")
public class TestResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long testCaseId;

    @Column(nullable = false)
    private Long testRunId;

    @Column(nullable = false)
    private Long testerId;

    @Column(nullable = false, length = 50)
    private String status;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(nullable = false)
    private LocalDateTime executedAt;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getTestCaseId() { return testCaseId; }
    public void setTestCaseId(Long testCaseId) { this.testCaseId = testCaseId; }
    public Long getTestRunId() { return testRunId; }
    public void setTestRunId(Long testRunId) { this.testRunId = testRunId; }
    public Long getTesterId() { return testerId; }
    public void setTesterId(Long testerId) { this.testerId = testerId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public LocalDateTime getExecutedAt() { return executedAt; }
    public void setExecutedAt(LocalDateTime executedAt) { this.executedAt = executedAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}