package com.iu.require4testing.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity-Klasse, die einen Testlauf (TestRun) repräsentiert.
 * <p>
 * Ein Testlauf ist eine Instanz der Durchführung von Tests gegen eine bestimmte Anforderung.
 * Er bündelt mehrere Testfälle (über {@link TestCaseTestRun}) und besitzt einen eigenen Status.
 * Jeder Testlauf ist genau einer {@link Requirement} zugeordnet.
 * </p>
 *
 * @author Require4Testing Team
 * @version 3.0.0
 */
@Entity
@Table(name = "test_runs")
public class TestRun {

    /**
     * Der technische Primärschlüssel der Datenbanktabelle.
     * Wird automatisch generiert.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Eine sprechende, für Menschen lesbare ID (z.B. TR-2025-0001).
     * Muss im System eindeutig sein.
     */
    @Column(unique = true)
    private String readableId;

    /**
     * Der Name oder Titel des Testlaufs (z.B. "Sprint 1 Regression").
     * Darf nicht null sein.
     */
    @Column(nullable = false, length = 500)
    private String name;

    /**
     * Eine ausführliche Beschreibung des Testlaufs.
     * Wird als Text-Blob (CLOB/TEXT) gespeichert.
     */
    @Column(columnDefinition = "TEXT")
    private String description;

    /**
     * Der aktuelle Status des Testlaufs.
     * Gültige Werte sind z.B. "PLANNING", "IN_PROGRESS", "SUCCESSFUL", "FAILED".
     */
    @Column(nullable = false, length = 50)
    private String status;

    /**
     * Die ID des Benutzers, der diesen Datensatz erstellt hat.
     */
    @Column(nullable = false)
    private Long createdBy;

    /**
     * Der Zeitstempel der Erstellung.
     * Wird automatisch gesetzt und ist nicht änderbar.
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Der Zeitstempel der letzten Aktualisierung.
     * Wird bei jeder Änderung automatisch aktualisiert.
     */
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Die verknüpfte Anforderung (Requirement).
     * Definiert eine Many-to-One Beziehung (Ein Testlauf gehört zu einer Anforderung).
     * Diese Verknüpfung ist zwingend erforderlich.
     */
    @ManyToOne
    @JoinColumn(name = "requirement_id", nullable = false)
    private Requirement requirement;

    /**
     * Die Liste der Testfall-Ausführungen in diesem Lauf.
     * Definiert eine One-to-Many Beziehung.
     * Bei Löschung des Testlaufs werden auch alle zugehörigen Ausführungen gelöscht (Cascade).
     */
    @OneToMany(mappedBy = "testRun", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TestCaseTestRun> testCaseTestRuns = new ArrayList<>();

    /**
     * Lifecycle-Methode: Wird vor dem ersten Persistieren aufgerufen.
     * Initialisiert Zeitstempel und Standardwerte für Status und Ersteller.
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) status = "PLANNING";
        if (createdBy == null) createdBy = 1L;
    }

    /**
     * Lifecycle-Methode: Wird vor jedem Update aufgerufen.
     * Aktualisiert den Zeitstempel der letzten Änderung.
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // --- Getter und Setter ---

    /**
     * Liefert die ID.
     * @return ID als Long.
     */
    public Long getId() { return id; }

    /**
     * Setzt die ID.
     * @param id ID als Long.
     */
    public void setId(Long id) { this.id = id; }

    /**
     * Liefert die sprechende ID.
     * @return Readable ID als String.
     */
    public String getReadableId() { return readableId; }

    /**
     * Setzt die sprechende ID.
     * @param readableId Readable ID als String.
     */
    public void setReadableId(String readableId) { this.readableId = readableId; }

    /**
     * Liefert den Namen.
     * @return Name als String.
     */
    public String getName() { return name; }

    /**
     * Setzt den Namen.
     * @param name Name als String.
     */
    public void setName(String name) { this.name = name; }

    /**
     * Gibt den Titel zurück (Alias für getName()).
     * Nützlich für generische UI-Komponenten.
     * @return Name als String.
     */
    public String getTitle() { return name; }

    /**
     * Liefert die Beschreibung.
     * @return Beschreibung als String.
     */
    public String getDescription() { return description; }

    /**
     * Setzt die Beschreibung.
     * @param description Beschreibung als String.
     */
    public void setDescription(String description) { this.description = description; }

    /**
     * Liefert den Status.
     * @return Status als String.
     */
    public String getStatus() { return status; }

    /**
     * Setzt den Status.
     * @param status Status als String.
     */
    public void setStatus(String status) { this.status = status; }

    /**
     * Liefert die Ersteller-ID.
     * @return User-ID als Long.
     */
    public Long getCreatedBy() { return createdBy; }

    /**
     * Setzt die Ersteller-ID.
     * @param createdBy User-ID als Long.
     */
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }

    /**
     * Liefert das Erstellungsdatum.
     * @return Datum als LocalDateTime.
     */
    public LocalDateTime getCreatedAt() { return createdAt; }

    /**
     * Setzt das Erstellungsdatum.
     * @param createdAt Datum als LocalDateTime.
     */
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    /**
     * Liefert das Änderungsdatum.
     * @return Datum als LocalDateTime.
     */
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    /**
     * Setzt das Änderungsdatum.
     * @param updatedAt Datum als LocalDateTime.
     */
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    /**
     * Liefert das zugehörige Requirement.
     * @return Requirement Entity.
     */
    public Requirement getRequirement() { return requirement; }

    /**
     * Setzt das zugehörige Requirement.
     * @param requirement Requirement Entity.
     */
    public void setRequirement(Requirement requirement) { this.requirement = requirement; }

    /**
     * Liefert die Liste der Testausführungen.
     * @return Liste von TestCaseTestRun.
     */
    public List<TestCaseTestRun> getTestCaseTestRuns() { return testCaseTestRuns; }

    /**
     * Setzt die Liste der Testausführungen.
     * @param testCaseTestRuns Liste von TestCaseTestRun.
     */
    public void setTestCaseTestRuns(List<TestCaseTestRun> testCaseTestRuns) { this.testCaseTestRuns = testCaseTestRuns; }
}