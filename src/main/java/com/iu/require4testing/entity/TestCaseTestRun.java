package com.iu.require4testing.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity-Klasse für die Many-to-Many-Zuordnung zwischen Testfällen und Testläufen.
  * Diese Klasse speichert den Zustand einer Testausführung innerhalb eines spezifischen Laufs.
 * Hier werden das Ergebnis (Status), Anmerkungen des Testers und der exakte Zeitpunkt der Durchführung persistiert.
 *
 * @author Require4Testing Team
 * @version 1.6.0
 */
@Entity
@Table(name = "test_case_test_run")
public class TestCaseTestRun {

    // Hinweis: JPA/Hibernate benötigt einen No-Args-Konstruktor.
    // Wir definieren ihn explizit (protected), um die Vorgaben klar zu erfüllen,
    // ohne das Verhalten zu verändern.
    public TestCaseTestRun() {
        // absichtlich leer
    }

    /**
     * Die eindeutige technische Identifikationsnummer dieser Ausführung.
     * Dient als Primärschlüssel und wird automatisch generiert.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Der Testfall, der ausgeführt wird.
     * Verweist auf die TestCase-Entität.
     */
    @ManyToOne
    @JoinColumn(name = "test_case_id", nullable = false)
    private TestCase testCase;

    /**
     * Der Testlauf, in dem dieser Testfall stattfindet.
     * Verweist auf die TestRun-Entität.
     */
    @ManyToOne
    @JoinColumn(name = "test_run_id", nullable = false)
    private TestRun testRun;

    /**
     * Der Benutzer (Tester), dem diese Ausführung zugewiesen wurde.
     * Verweist auf die User-Entität.
     */
    @ManyToOne
    @JoinColumn(name = "tester_id")
    private User tester;

    /**
     * Der aktuelle Status der Durchführung.
     * Erlaubte Werte sind typischerweise ASSIGNED, PASSED oder FAILED.
     */
    @Column(name = "status", nullable = false)
    private String status;

    /**
     * Optionale Notizen, Fehlerbeschreibungen oder Kommentare des Testers zur Durchführung.
     */
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    /**
     * Der exakte Zeitpunkt, an dem die Testausführung durchgeführt oder das Ergebnis gespeichert wurde.
     */
    @Column(name = "executed_at")
    private LocalDateTime executedAt;

    /**
     * Gibt die technische ID der Zuordnung zurück.
     * @return Die ID als Long.
     */
    public Long getId() {
        return id;
    }

    /**
     * Legt die technische ID der Zuordnung fest.
     * @param id Die neue technische ID.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Liefert den zugeordneten Testfall zurück.
     * @return Das TestCase-Objekt.
     */
    public TestCase getTestCase() {
        return testCase;
    }

    /**
     * Verknüpft einen Testfall mit dieser Ausführung.
     * @param testCase Der auszuführende Testfall.
     */
    public void setTestCase(TestCase testCase) {
        this.testCase = testCase;
    }

    /**
     * Liefert den zugeordneten Testlauf zurück.
     * @return Das TestRun-Objekt.
     */
    public TestRun getTestRun() {
        return testRun;
    }

    /**
     * Ordnet diese Ausführung einem Testlauf zu.
     * @param testRun Der übergeordnete Testlauf.
     */
    public void setTestRun(TestRun testRun) {
        this.testRun = testRun;
    }

    /**
     * Liefert den zuständigen Tester zurück.
     * @return Das User-Objekt des Testers.
     */
    public User getTester() {
        return tester;
    }

    /**
     * Weist diese Ausführung einem bestimmten Tester zu.
     * @param tester Der ausführende Benutzer.
     */
    public void setTester(User tester) {
        this.tester = tester;
    }

    /**
     * Gibt den aktuellen Status der Testdurchführung zurück.
     * @return Der Status als String (z. B. "PASSED").
     */
    public String getStatus() {
        return status;
    }

    /**
     * Aktualisiert den Status der Testausführung.
     * @param status Der neue Status-String.
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Liefert die hinterlegten Notizen des Testers zurück.
     * @return Die Notizen als String oder null.
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Ermöglicht das Speichern von Kommentaren zur Durchführung.
     * @param notes Die neuen Notizen des Testers.
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * Liefert den Ausführungszeitpunkt zurück.
     * @return Der Zeitpunkt als LocalDateTime oder null.
     */
    public LocalDateTime getExecutedAt() {
        return executedAt;
    }

    /**
     * Legt den Zeitpunkt der Testdurchführung fest.
     * @param executedAt Der Ausführungszeitpunkt.
     */
    public void setExecutedAt(LocalDateTime executedAt) {
        this.executedAt = executedAt;
    }
}