package com.iu.require4testing.service;

import com.iu.require4testing.entity.TestCaseTestRun;
import com.iu.require4testing.exception.ResourceNotFoundException;
import com.iu.require4testing.repository.TestCaseTestRunRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service für TestCaseTestRun-Operationen.
 * Bietet Geschäftslogik für die Zuordnung zwischen Testfällen und Testläufen.
 */
@Service
@Transactional
public class TestCaseTestRunService {
    
    private final TestCaseTestRunRepository testCaseTestRunRepository;
    
    @Autowired
    public TestCaseTestRunService(TestCaseTestRunRepository testCaseTestRunRepository) {
        this.testCaseTestRunRepository = testCaseTestRunRepository;
    }
    
    /**
     * Gibt alle Zuordnungen zurück.
     * 
     * @return Liste aller Zuordnungen
     */
    public List<TestCaseTestRun> getAllTestCaseTestRuns() {
        return testCaseTestRunRepository.findAll();
    }
    
    /**
     * Findet eine Zuordnung anhand ihrer ID.
     * 
     * @param id Die Zuordnungs-ID
     * @return Die Zuordnung
     * @throws ResourceNotFoundException wenn die Zuordnung nicht gefunden wird
     */
    public TestCaseTestRun getTestCaseTestRunById(Long id) {
        return testCaseTestRunRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TestCaseTestRun", "ID", id));
    }
    
    /**
     * Findet alle Zuordnungen für einen bestimmten Testfall.
     * 
     * @param testCaseId Die ID des Testfalls
     * @return Liste der Zuordnungen
     */
    public List<TestCaseTestRun> getByTestCaseId(Long testCaseId) {
        return testCaseTestRunRepository.findByTestCaseId(testCaseId);
    }
    
    /**
     * Findet alle Zuordnungen für einen bestimmten Testlauf.
     * 
     * @param testRunId Die ID des Testlaufs
     * @return Liste der Zuordnungen
     */
    public List<TestCaseTestRun> getByTestRunId(Long testRunId) {
        return testCaseTestRunRepository.findByTestRunId(testRunId);
    }
    
    /**
     * Findet alle Zuordnungen für einen bestimmten Tester.
     * 
     * @param testerId Die ID des Testers
     * @return Liste der Zuordnungen
     */
    public List<TestCaseTestRun> getByTesterId(Long testerId) {
        return testCaseTestRunRepository.findByTesterId(testerId);
    }
    
    /**
     * Erstellt eine neue Zuordnung.
     * 
     * @param testCaseTestRun Die Zuordnungsdaten
     * @return Die erstellte Zuordnung
     */
    public TestCaseTestRun createTestCaseTestRun(TestCaseTestRun testCaseTestRun) {
        if (testCaseTestRunRepository.existsByTestCaseIdAndTestRunId(
                testCaseTestRun.getTestCaseId(), testCaseTestRun.getTestRunId())) {
            throw new IllegalArgumentException("Zuordnung existiert bereits");
        }
        return testCaseTestRunRepository.save(testCaseTestRun);
    }
    
    /**
     * Aktualisiert eine bestehende Zuordnung.
     * 
     * @param id Die Zuordnungs-ID
     * @param testCaseTestRun Die neuen Zuordnungsdaten
     * @return Die aktualisierte Zuordnung
     * @throws ResourceNotFoundException wenn die Zuordnung nicht gefunden wird
     */
    public TestCaseTestRun updateTestCaseTestRun(Long id, TestCaseTestRun testCaseTestRun) {
        TestCaseTestRun existing = testCaseTestRunRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TestCaseTestRun", "ID", id));
        
        existing.setTesterId(testCaseTestRun.getTesterId());
        return testCaseTestRunRepository.save(existing);
    }
    
    /**
     * Löscht eine Zuordnung.
     * 
     * @param id Die Zuordnungs-ID
     * @throws ResourceNotFoundException wenn die Zuordnung nicht gefunden wird
     */
    public void deleteTestCaseTestRun(Long id) {
        if (!testCaseTestRunRepository.existsById(id)) {
            throw new ResourceNotFoundException("TestCaseTestRun", "ID", id);
        }
        testCaseTestRunRepository.deleteById(id);
    }
}
