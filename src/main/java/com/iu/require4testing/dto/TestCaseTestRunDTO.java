package com.iu.require4testing.dto;

/**
 * Data Transfer Object (DTO) für die Zuordnung von Testfällen zu Testläufen (TestCaseTestRun).
 * <p>
 * Dieses DTO dient dem Datenaustausch über die REST-Schnittstelle und zwischen den Schichten.
 * Es kapselt die Informationen einer Testausführung, wie die IDs der beteiligten Entitäten
 * und den aktuellen Status.
 * </p>
 *
 * @author Require4Testing Team
 * @version 1.0.0
 */
public class TestCaseTestRunDTO {

    /**
     * Die eindeutige Identifikationsnummer der Zuordnung.
     * Dient als Primärschlüssel.
     */
    private Long id;

    /**
     * Die ID des zugeordneten Testfalls (TestCase).
     * Referenziert den Testfall, der in diesem Lauf ausgeführt wird.
     */
    private Long testCaseId;

    /**
     * Die ID des zugehörigen Testlaufs (TestRun).
     * Referenziert den Testlauf, zu dem diese Ausführung gehört.
     */
    private Long testRunId;

    /**
     * Die ID des zugewiesenen Testers (User).
     * Referenziert den Benutzer, der den Test durchführt. Kann {@code null} sein.
     */
    private Long testerId;

    /**
     * Der aktuelle Status der Testdurchführung.
     * Typische Werte: "OPEN" (Offen), "PASSED" (Bestanden), "FAILED" (Fehlgeschlagen).
     */
    private String status;

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
    public Long getId() { return id; }

    /**
     * Setzt die ID der Zuordnung.
     *
     * @param id Die zu setzende ID.
     */
    public void setId(Long id) { this.id = id; }

    /**
     * Gibt die ID des verknüpften Testfalls zurück.
     *
     * @return Die Testfall-ID als Long.
     */
    public Long getTestCaseId() { return testCaseId; }

    /**
     * Setzt die ID des verknüpften Testfalls.
     *
     * @param testCaseId Die zu setzende Testfall-ID.
     */
    public void setTestCaseId(Long testCaseId) { this.testCaseId = testCaseId; }

    /**
     * Gibt die ID des verknüpften Testlaufs zurück.
     *
     * @return Die Testlauf-ID als Long.
     */
    public Long getTestRunId() { return testRunId; }

    /**
     * Setzt die ID des verknüpften Testlaufs.
     *
     * @param testRunId Die zu setzende Testlauf-ID.
     */
    public void setTestRunId(Long testRunId) { this.testRunId = testRunId; }

    /**
     * Gibt die ID des zugewiesenen Testers zurück.
     *
     * @return Die Tester-ID als Long oder null.
     */
    public Long getTesterId() { return testerId; }

    /**
     * Setzt die ID des zugewiesenen Testers.
     *
     * @param testerId Die zu setzende Tester-ID.
     */
    public void setTesterId(Long testerId) { this.testerId = testerId; }

    /**
     * Gibt den aktuellen Status der Durchführung zurück.
     *
     * @return Der Status als String.
     */
    public String getStatus() { return status; }

    /**
     * Setzt den aktuellen Status der Durchführung.
     *
     * @param status Der zu setzende Status.
     */
    public void setStatus(String status) { this.status = status; }
}