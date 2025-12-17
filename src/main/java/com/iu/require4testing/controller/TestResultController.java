package com.iu.require4testing.controller;

import com.iu.require4testing.dto.TestResultDTO;
import com.iu.require4testing.service.TestResultService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST-Controller für die Verwaltung von Testergebnissen (TestResult).
 * <p>
 * Dieser Controller bietet Endpunkte zum Erstellen, Lesen, Aktualisieren und Löschen
 * von historischen oder detaillierten Testergebnissen.
 * </p>
 *
 * @author Require4Testing Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/test-results")
public class TestResultController {

    /**
     * Service für die Geschäftslogik der Testergebnisse.
     */
    private final TestResultService testResultService;

    /**
     * Erstellt den Controller und injiziert den {@link TestResultService}.
     *
     * <p>Hinweis: Bei genau einem Konstruktor ist in Spring keine zusätzliche Annotation nötig.</p>
     *
     * @param testResultService Der Service für Testergebnisse.
     */
    public TestResultController(TestResultService testResultService) {
        this.testResultService = testResultService;
    }

    /**
     * Ruft alle Testergebnisse ab.
     *
     * @return Liste aller TestResultDTOs.
     */
    @GetMapping
    public ResponseEntity<List<TestResultDTO>> getAllTestResults() {
        List<TestResultDTO> testResults = testResultService.getAllTestResults();
        return ResponseEntity.ok(testResults);
    }

    /**
     * Ruft ein Testergebnis per ID ab.
     *
     * @param id Die ID des Ergebnisses.
     * @return Das gefundene DTO.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TestResultDTO> getTestResultById(@PathVariable Long id) {
        TestResultDTO testResult = testResultService.getTestResultById(id);
        return ResponseEntity.ok(testResult);
    }

    /**
     * Sucht Ergebnisse für einen bestimmten Testfall.
     *
     * @param testCaseId Die ID des Testfalls.
     * @return Liste der Ergebnisse.
     */
    @GetMapping("/test-case/{testCaseId}")
    public ResponseEntity<List<TestResultDTO>> getTestResultsByTestCase(@PathVariable Long testCaseId) {
        List<TestResultDTO> testResults = testResultService.getTestResultsByTestCase(testCaseId);
        return ResponseEntity.ok(testResults);
    }

    /**
     * Sucht Ergebnisse aus einem bestimmten Testlauf.
     *
     * @param testRunId Die ID des Testlaufs.
     * @return Liste der Ergebnisse.
     */
    @GetMapping("/test-run/{testRunId}")
    public ResponseEntity<List<TestResultDTO>> getTestResultsByTestRun(@PathVariable Long testRunId) {
        List<TestResultDTO> testResults = testResultService.getTestResultsByTestRun(testRunId);
        return ResponseEntity.ok(testResults);
    }

    /**
     * Sucht Ergebnisse eines bestimmten Testers.
     *
     * @param testerId Die ID des Testers.
     * @return Liste der Ergebnisse.
     */
    @GetMapping("/tester/{testerId}")
    public ResponseEntity<List<TestResultDTO>> getTestResultsByTester(@PathVariable Long testerId) {
        List<TestResultDTO> testResults = testResultService.getTestResultsByTester(testerId);
        return ResponseEntity.ok(testResults);
    }

    /**
     * Erstellt ein neues Testergebnis.
     *
     * @param testResultDTO Das DTO mit den Ergebnisdaten.
     * @return Das erstellte DTO.
     */
    @PostMapping
    public ResponseEntity<TestResultDTO> createTestResult(@Valid @RequestBody TestResultDTO testResultDTO) {
        TestResultDTO createdTestResult = testResultService.createTestResult(testResultDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTestResult);
    }

    /**
     * Aktualisiert ein Testergebnis.
     *
     * @param id Die ID des Ergebnisses.
     * @param testResultDTO Die neuen Daten.
     * @return Das aktualisierte DTO.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TestResultDTO> updateTestResult(@PathVariable Long id,
                                                          @Valid @RequestBody TestResultDTO testResultDTO) {
        TestResultDTO updatedTestResult = testResultService.updateTestResult(id, testResultDTO);
        return ResponseEntity.ok(updatedTestResult);
    }

    /**
     * Löscht ein Testergebnis.
     *
     * @param id Die ID des zu löschenden Ergebnisses.
     * @return Leere Antwort.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTestResult(@PathVariable Long id) {
        testResultService.deleteTestResult(id);
        return ResponseEntity.noContent().build();
    }
}