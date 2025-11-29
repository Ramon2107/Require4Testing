package com.iu.require4testing.controller;

import com.iu.require4testing.dto.TestResultDTO;
import com.iu.require4testing.service.TestResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST-Controller für Testergebnis-Endpunkte.
 * Bietet CRUD-Operationen für Testergebnisse über die REST-API.
 */
@RestController
@RequestMapping("/api/test-results")
public class TestResultController {
    
    private final TestResultService testResultService;
    
    @Autowired
    public TestResultController(TestResultService testResultService) {
        this.testResultService = testResultService;
    }
    
    /**
     * Gibt alle Testergebnisse zurück.
     * 
     * @return Liste aller Testergebnisse
     */
    @GetMapping
    public ResponseEntity<List<TestResultDTO>> getAllTestResults() {
        List<TestResultDTO> testResults = testResultService.getAllTestResults();
        return ResponseEntity.ok(testResults);
    }
    
    /**
     * Gibt ein Testergebnis anhand seiner ID zurück.
     * 
     * @param id Die Testergebnis-ID
     * @return Das Testergebnis
     */
    @GetMapping("/{id}")
    public ResponseEntity<TestResultDTO> getTestResultById(@PathVariable Long id) {
        TestResultDTO testResult = testResultService.getTestResultById(id);
        return ResponseEntity.ok(testResult);
    }
    
    /**
     * Gibt alle Testergebnisse eines bestimmten Testfalls zurück.
     * 
     * @param testCaseId Die ID des Testfalls
     * @return Liste der Testergebnisse
     */
    @GetMapping("/test-case/{testCaseId}")
    public ResponseEntity<List<TestResultDTO>> getTestResultsByTestCase(@PathVariable Long testCaseId) {
        List<TestResultDTO> testResults = testResultService.getTestResultsByTestCase(testCaseId);
        return ResponseEntity.ok(testResults);
    }
    
    /**
     * Gibt alle Testergebnisse eines bestimmten Testlaufs zurück.
     * 
     * @param testRunId Die ID des Testlaufs
     * @return Liste der Testergebnisse
     */
    @GetMapping("/test-run/{testRunId}")
    public ResponseEntity<List<TestResultDTO>> getTestResultsByTestRun(@PathVariable Long testRunId) {
        List<TestResultDTO> testResults = testResultService.getTestResultsByTestRun(testRunId);
        return ResponseEntity.ok(testResults);
    }
    
    /**
     * Gibt alle Testergebnisse eines bestimmten Testers zurück.
     * 
     * @param testerId Die ID des Testers
     * @return Liste der Testergebnisse
     */
    @GetMapping("/tester/{testerId}")
    public ResponseEntity<List<TestResultDTO>> getTestResultsByTester(@PathVariable Long testerId) {
        List<TestResultDTO> testResults = testResultService.getTestResultsByTester(testerId);
        return ResponseEntity.ok(testResults);
    }
    
    /**
     * Erstellt ein neues Testergebnis.
     * 
     * @param testResultDTO Die Testergebnisdaten
     * @return Das erstellte Testergebnis
     */
    @PostMapping
    public ResponseEntity<TestResultDTO> createTestResult(@RequestBody TestResultDTO testResultDTO) {
        TestResultDTO createdTestResult = testResultService.createTestResult(testResultDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTestResult);
    }
    
    /**
     * Aktualisiert ein bestehendes Testergebnis.
     * 
     * @param id Die Testergebnis-ID
     * @param testResultDTO Die neuen Testergebnisdaten
     * @return Das aktualisierte Testergebnis
     */
    @PutMapping("/{id}")
    public ResponseEntity<TestResultDTO> updateTestResult(@PathVariable Long id, 
                                                          @RequestBody TestResultDTO testResultDTO) {
        TestResultDTO updatedTestResult = testResultService.updateTestResult(id, testResultDTO);
        return ResponseEntity.ok(updatedTestResult);
    }
    
    /**
     * Löscht ein Testergebnis.
     * 
     * @param id Die Testergebnis-ID
     * @return Leere Antwort
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTestResult(@PathVariable Long id) {
        testResultService.deleteTestResult(id);
        return ResponseEntity.noContent().build();
    }
}
