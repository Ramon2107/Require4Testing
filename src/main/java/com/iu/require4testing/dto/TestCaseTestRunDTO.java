package com.iu.require4testing.dto;

import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) für die Zuordnung von Testfällen zu Testläufen (TestCaseTestRun).
 *
 * <p>
 * Das Testergebnis wird direkt an der Ausführung gespeichert,
 * also in dieser Zuordnung. Ein separates Testergebnis-Objekt ist für den Prototyp nicht notwendig.
 * </p>
 *
 * <p>
 * Für die Bewertung werden die Stati {@code ASSIGNED}, {@code PASSED} und {@code FAILED} verwendet.
 * </p>
 *
 * @author Require4Testing Team
 * @version 1.1.0
 */
public class TestCaseTestRunDTO {

    /**
     * Die eindeutige Identifikationsnummer der Zuordnung.
     * Dient als Primärschlüssel.
     */
    private Long id;

    /**
     * Die ID des zugeordneten Testfalls (TestCase).
     */
    private Long testCaseId;

    /**
     * Die ID des zugehörigen Testlaufs (TestRun).
     */
    private Long testRunId;

    /**
     * Die ID des zugewiesenen Testers (User).
     * Kann {@code null} sein.
     */
    private Long testerId;

    /**
     * Der aktuelle Status der Testdurchführung.
     *
     * <p>Erwartete Werte: {@code ASSIGNED}, {@code PASSED}, {@code FAILED}.</p>
     */
    private String status;

    /**
     * Optionale Notiz des Testers zur Ausführung.
     */
    private String notes;

    /**
     * Zeitpunkt der Ausführung (typischerweise gesetzt, wenn der Status auf PASSED/FAILED gesetzt wird).
     */
    private LocalDateTime executedAt;

    /**
     * Standardkonstruktor.
     * Notwendig für die Instanziierung durch Frameworks.
     */
    public TestCaseTestRunDTO() {}

    // --- Getter und Setter ---

    /**
     * Gibt die ID der Zuordnung zurück.
     *
     * @return Die ID als Long.
     */
    public Long getId() {
        return id;
    }

    /**
     * Setzt die ID der Zuordnung.
     *
     * @param id Die zu setzende ID.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gibt die ID des verknüpften Testfalls zurück.
     *
     * @return Die Testfall-ID als Long.
     */
    public Long getTestCaseId() {
        return testCaseId;
    }

    /**
     * Setzt die ID des verknüpften Testfalls.
     *
     * @param testCaseId Die zu setzende Testfall-ID.
     */
    public void setTestCaseId(Long testCaseId) {
        this.testCaseId = testCaseId;
    }

    /**
     * Gibt die ID des verknüpften Testlaufs zurück.
     *
     * @return Die Testlauf-ID als Long.
     */
    public Long getTestRunId() {
        return testRunId;
    }

    /**
     * Setzt die ID des verknüpften Testlaufs.
     *
     * @param testRunId Die zu setzende Testlauf-ID.
     */
    public void setTestRunId(Long testRunId) {
        this.testRunId = testRunId;
    }

    /**
     * Gibt die ID des zugewiesenen Testers zurück.
     *
     * @return Die Tester-ID als Long oder {@code null}.
     */
    public Long getTesterId() {
        return testerId;
    }

    /**
     * Setzt die ID des zugewiesenen Testers.
     *
     * @param testerId Die zu setzende Tester-ID.
     */
    public void setTesterId(Long testerId) {
        this.testerId = testerId;
    }

    /**
     * Gibt den aktuellen Status der Durchführung zurück.
     *
     * @return Der Status als String.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Setzt den aktuellen Status der Durchführung.
     *
     * @param status Der zu setzende Status.
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Gibt die Notiz des Testers zurück.
     *
     * @return Notiz oder {@code null}
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Setzt die Notiz des Testers.
     *
     * @param notes Notiz (optional)
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * Gibt den Ausführungszeitpunkt zurück.
     *
     * @return Ausführungszeitpunkt oder {@code null}
     */
    public LocalDateTime getExecutedAt() {
        return executedAt;
    }

    /**
     * Setzt den Ausführungszeitpunkt.
     *
     * @param executedAt Ausführungszeitpunkt (optional)
     */
    public void setExecutedAt(LocalDateTime executedAt) {
        this.executedAt = executedAt;
    }
}