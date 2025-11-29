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
    
    /**
     * Findet alle Zuordnungen für einen bestimmten Testfall.
     * 
     * @param testCaseId Die ID des Testfalls
     * @return Liste der Zuordnungen
     */
    List<TestCaseTestRun> findByTestCaseId(Long testCaseId);
    
    /**
     * Findet alle Zuordnungen für einen bestimmten Testlauf.
     * 
     * @param testRunId Die ID des Testlaufs
     * @return Liste der Zuordnungen
     */
    List<TestCaseTestRun> findByTestRunId(Long testRunId);
    
    /**
     * Findet alle Zuordnungen für einen bestimmten Tester.
     * 
     * @param testerId Die ID des Testers
     * @return Liste der Zuordnungen
     */
    List<TestCaseTestRun> findByTesterId(Long testerId);
    
    /**
     * Findet eine spezifische Zuordnung.
     * 
     * @param testCaseId Die ID des Testfalls
     * @param testRunId Die ID des Testlaufs
     * @return Optional mit der Zuordnung, falls gefunden
     */
    Optional<TestCaseTestRun> findByTestCaseIdAndTestRunId(Long testCaseId, Long testRunId);
    
    /**
     * Prüft, ob eine Zuordnung existiert.
     * 
     * @param testCaseId Die ID des Testfalls
     * @param testRunId Die ID des Testlaufs
     * @return true, wenn die Zuordnung existiert
     */
    boolean existsByTestCaseIdAndTestRunId(Long testCaseId, Long testRunId);
}
