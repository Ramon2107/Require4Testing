package com.iu.require4testing.entity;

import jakarta.persistence.*;

/**
 * Entity für die Many-to-Many-Zuordnung zwischen Testfällen und Testläufen.
 * <p>
 * Diese Klasse repräsentiert die Ausführung eines Testfalls innerhalb eines spezifischen Testlaufs.
 * Sie speichert zusätzlich, welcher Tester zugeordnet ist und den aktuellen Status der Durchführung.
 * </p>
 *
 * @author Require4Testing Team
 * @version 1.0.0
 */
@Entity
@Table(name = "test_case_test_run")
public class TestCaseTestRun {

    /** Eindeutige ID der Zuordnung. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Der auszuführende Testfall. */
    @ManyToOne
    @JoinColumn(name = "test_case_id", nullable = false)
    private TestCase testCase;

    /** Der Testlauf, in dem dieser Testfall ausgeführt wird. */
    @ManyToOne
    @JoinColumn(name = "test_run_id", nullable = false)
    private TestRun testRun;

    /** Der Tester (User), dem dieser Testfall zugewiesen wurde. */
    @ManyToOne
    @JoinColumn(name = "tester_id")
    private User tester;

    /** Der Status der Ausführung (z.B. "OPEN", "PASSED", "FAILED"). */
    private String status;

    // --- Getter und Setter ---

    /**
     * Gibt die ID der Zuordnung zurück.
     * @return die ID
     */
    public Long getId() { return id; }

    /**
     * Setzt die ID der Zuordnung.
     * @param id die neue ID
     */
    public void setId(Long id) { this.id = id; }

    /**
     * Gibt den zugeordneten Testfall zurück.
     * @return das TestCase Objekt
     */
    public TestCase getTestCase() { return testCase; }

    /**
     * Setzt den zugeordneten Testfall.
     * @param testCase das neue TestCase Objekt
     */
    public void setTestCase(TestCase testCase) { this.testCase = testCase; }

    /**
     * Gibt den Testlauf zurück.
     * @return das TestRun Objekt
     */
    public TestRun getTestRun() { return testRun; }

    /**
     * Setzt den Testlauf.
     * @param testRun das neue TestRun Objekt
     */
    public void setTestRun(TestRun testRun) { this.testRun = testRun; }

    /**
     * Gibt den zugewiesenen Tester zurück.
     * @return das User Objekt
     */
    public User getTester() { return tester; }

    /**
     * Setzt den zugewiesenen Tester.
     * @param tester das neue User Objekt
     */
    public void setTester(User tester) { this.tester = tester; }

    /**
     * Gibt den Status der Ausführung zurück.
     * @return der Status als String
     */
    public String getStatus() { return status; }

    /**
     * Setzt den Status der Ausführung.
     * @param status der neue Status (z.B. "PASSED")
     */
    public void setStatus(String status) { this.status = status; }
}