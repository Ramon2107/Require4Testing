package com.iu.require4testing.repository;

import com.iu.require4testing.entity.TestCaseTestRun;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository für TestCaseTestRun-Entitäten.
 * Bietet CRUD-Operationen für die Zuordnung zwischen Testfällen und Testläufen.
 */
@Repository
public interface TestCaseTestRunRepository extends JpaRepository<TestCaseTestRun, Long> {

    List<TestCaseTestRun> findByTestCaseId(Long testCaseId);

    List<TestCaseTestRun> findByTestRunId(Long testRunId);

    List<TestCaseTestRun> findByTesterId(Long testerId);

    List<TestCaseTestRun> findByTesterIdAndStatus(Long testerId, String status);

    /**
     * Prüft effizient, ob es innerhalb einer Anforderung mindestens einen fehlgeschlagenen Test gibt.
     *
     * @param requirementId Die ID der Anforderung
     * @param status Der zu prüfende Status (typischerweise {@code FAILED})
     * @return {@code true}, wenn ein Eintrag existiert, sonst {@code false}
     */
    boolean existsByTestRun_Requirement_IdAndStatus(Long requirementId, String status);

    /**
     * Prüft effizient, ob es innerhalb einer Anforderung mindestens eine Zuordnung gibt, die einem Tester zugewiesen ist.
     *
     * @param requirementId Die ID der Anforderung
     * @return {@code true}, wenn eine Zuordnung mit Tester existiert, sonst {@code false}
     */
    boolean existsByTestRun_Requirement_IdAndTesterIsNotNull(Long requirementId);

    /**
     * Prüft effizient, ob innerhalb einer Anforderung mindestens eine zugewiesene Ausführung existiert,
     * die noch nicht {@code PASSED} ist (z.B. {@code ASSIGNED}).
     *
     * @param requirementId Die ID der Anforderung
     * @param status Der Status, der ausgeschlossen werden soll (typischerweise {@code PASSED})
     * @return {@code true}, wenn eine solche Ausführung existiert, sonst {@code false}
     */
    boolean existsByTestRun_Requirement_IdAndTesterIsNotNullAndStatusNot(Long requirementId, String status);

    Optional<TestCaseTestRun> findByTestCaseIdAndTestRunId(Long testCaseId, Long testRunId);

    boolean existsByTestCaseIdAndTestRunId(Long testCaseId, Long testRunId);
}