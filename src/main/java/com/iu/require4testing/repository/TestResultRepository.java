package com.iu.require4testing.repository;

import com.iu.require4testing.entity.TestResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository für Testergebnis-Entitäten.
 * Bietet CRUD-Operationen und benutzerdefinierte Abfragen für Testergebnisse.
 */
@Repository
public interface TestResultRepository extends JpaRepository<TestResult, Long> {
    
    /**
     * Findet alle Testergebnisse eines bestimmten Testfalls.
     * 
     * @param testCaseId Die ID des Testfalls
     * @return Liste der Testergebnisse
     */
    List<TestResult> findByTestCaseId(Long testCaseId);
    
    /**
     * Findet alle Testergebnisse eines bestimmten Testlaufs.
     * 
     * @param testRunId Die ID des Testlaufs
     * @return Liste der Testergebnisse
     */
    List<TestResult> findByTestRunId(Long testRunId);
    
    /**
     * Findet alle Testergebnisse eines bestimmten Testers.
     * 
     * @param testerId Die ID des Testers
     * @return Liste der Testergebnisse
     */
    List<TestResult> findByTesterId(Long testerId);
    
    /**
     * Findet Testergebnisse nach Status.
     * 
     * @param status Der Status
     * @return Liste der Testergebnisse
     */
    List<TestResult> findByStatus(String status);
    
    /**
     * Findet alle Testergebnisse für einen Testfall in einem bestimmten Testlauf.
     * 
     * @param testCaseId Die ID des Testfalls
     * @param testRunId Die ID des Testlaufs
     * @return Liste der Testergebnisse
     */
    List<TestResult> findByTestCaseIdAndTestRunId(Long testCaseId, Long testRunId);
}
