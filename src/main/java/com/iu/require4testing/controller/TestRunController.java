package com.iu.require4testing.controller;

import com.iu.require4testing.dto.TestRunDTO;
import com.iu.require4testing.service.TestRunService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST-Controller für Testlauf-Endpunkte.
 * Bietet CRUD-Operationen für Testläufe über die REST-API.
 * Implementiert Best Practices für RESTful Web Services.
 */
@RestController
@RequestMapping("/api/test-runs")
public class TestRunController {
    
    private final TestRunService testRunService;
    
    /**
     * Konstruktor mit Dependency Injection.
     * 
     * @param testRunService Der Testlauf-Service
     */
    @Autowired
    public TestRunController(TestRunService testRunService) {
        this.testRunService = testRunService;
    }
    
    /**
     * Gibt alle Testläufe zurück.
     * 
     * @return Liste aller Testläufe
     */
    @GetMapping
    public ResponseEntity<List<TestRunDTO>> getAllTestRuns() {
        List<TestRunDTO> testRuns = testRunService.getAllTestRuns();
        return ResponseEntity.ok(testRuns);
    }
    
    /**
     * Gibt einen Testlauf anhand seiner ID zurück.
     * 
     * @param id Die Testlauf-ID
     * @return Der Testlauf
     */
    @GetMapping("/{id}")
    public ResponseEntity<TestRunDTO> getTestRunById(@PathVariable Long id) {
        TestRunDTO testRun = testRunService.getTestRunById(id);
        return ResponseEntity.ok(testRun);
    }
    
    /**
     * Gibt alle Testläufe eines bestimmten Erstellers zurück.
     * 
     * @param createdBy Die ID des Erstellers
     * @return Liste der Testläufe
     */
    @GetMapping("/creator/{createdBy}")
    public ResponseEntity<List<TestRunDTO>> getTestRunsByCreator(@PathVariable Long createdBy) {
        List<TestRunDTO> testRuns = testRunService.getTestRunsByCreator(createdBy);
        return ResponseEntity.ok(testRuns);
    }
    
    /**
     * Gibt alle Testläufe mit einem bestimmten Status zurück.
     * 
     * @param status Der Status
     * @return Liste der Testläufe
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<TestRunDTO>> getTestRunsByStatus(@PathVariable String status) {
        List<TestRunDTO> testRuns = testRunService.getTestRunsByStatus(status);
        return ResponseEntity.ok(testRuns);
    }
    
    /**
     * Erstellt einen neuen Testlauf.
     * Die Eingabedaten werden automatisch validiert.
     * 
     * @param testRunDTO Die Testlaufdaten (validiert)
     * @return Der erstellte Testlauf
     */
    @PostMapping
    public ResponseEntity<TestRunDTO> createTestRun(@Valid @RequestBody TestRunDTO testRunDTO) {
        TestRunDTO createdTestRun = testRunService.createTestRun(testRunDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTestRun);
    }
    
    /**
     * Aktualisiert einen bestehenden Testlauf.
     * Die Eingabedaten werden automatisch validiert.
     * 
     * @param id Die Testlauf-ID
     * @param testRunDTO Die neuen Testlaufdaten (validiert)
     * @return Der aktualisierte Testlauf
     */
    @PutMapping("/{id}")
    public ResponseEntity<TestRunDTO> updateTestRun(@PathVariable Long id, 
                                                    @Valid @RequestBody TestRunDTO testRunDTO) {
        TestRunDTO updatedTestRun = testRunService.updateTestRun(id, testRunDTO);
        return ResponseEntity.ok(updatedTestRun);
    }
    
    /**
     * Löscht einen Testlauf.
     * 
     * @param id Die Testlauf-ID
     * @return Leere Antwort
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTestRun(@PathVariable Long id) {
        testRunService.deleteTestRun(id);
        return ResponseEntity.noContent().build();
    }
}
