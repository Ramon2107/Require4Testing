package com.iu.require4testing.controller;

import com.iu.require4testing.dto.TestCaseTestRunDTO;
import com.iu.require4testing.entity.TestCaseTestRun;
import com.iu.require4testing.service.TestCaseTestRunService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST-Controller für Testfall-Testlauf-Zuordnungen.
 * Ermöglicht das Zuordnen von Testfällen und Testern zu Testläufen.
 * Diese Funktion ist notwendig für Testmanager:innen, um Testfälle
 * und Tester:innen einem Testlauf zuzuweisen.
 */
@RestController
@RequestMapping("/api/test-case-test-runs")
public class TestCaseTestRunController {
    
    private final TestCaseTestRunService testCaseTestRunService;
    
    /**
     * Konstruktor mit Dependency Injection.
     * 
     * @param testCaseTestRunService Der Service für Testfall-Testlauf-Zuordnungen
     */
    @Autowired
    public TestCaseTestRunController(TestCaseTestRunService testCaseTestRunService) {
        this.testCaseTestRunService = testCaseTestRunService;
    }
    
    /**
     * Gibt alle Testfall-Testlauf-Zuordnungen zurück.
     * 
     * @return Liste aller Zuordnungen
     */
    @GetMapping
    public ResponseEntity<List<TestCaseTestRunDTO>> getAllTestCaseTestRuns() {
        List<TestCaseTestRunDTO> assignments = testCaseTestRunService.getAllTestCaseTestRuns()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(assignments);
    }
    
    /**
     * Gibt eine Zuordnung anhand ihrer ID zurück.
     * 
     * @param id Die Zuordnungs-ID
     * @return Die Zuordnung
     */
    @GetMapping("/{id}")
    public ResponseEntity<TestCaseTestRunDTO> getTestCaseTestRunById(@PathVariable Long id) {
        TestCaseTestRun assignment = testCaseTestRunService.getTestCaseTestRunById(id);
        return ResponseEntity.ok(convertToDTO(assignment));
    }
    
    /**
     * Gibt alle Zuordnungen für einen bestimmten Testfall zurück.
     * 
     * @param testCaseId Die ID des Testfalls
     * @return Liste der Zuordnungen
     */
    @GetMapping("/test-case/{testCaseId}")
    public ResponseEntity<List<TestCaseTestRunDTO>> getByTestCaseId(@PathVariable Long testCaseId) {
        List<TestCaseTestRunDTO> assignments = testCaseTestRunService.getByTestCaseId(testCaseId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(assignments);
    }
    
    /**
     * Gibt alle Zuordnungen für einen bestimmten Testlauf zurück.
     * 
     * @param testRunId Die ID des Testlaufs
     * @return Liste der Zuordnungen
     */
    @GetMapping("/test-run/{testRunId}")
    public ResponseEntity<List<TestCaseTestRunDTO>> getByTestRunId(@PathVariable Long testRunId) {
        List<TestCaseTestRunDTO> assignments = testCaseTestRunService.getByTestRunId(testRunId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(assignments);
    }
    
    /**
     * Gibt alle Zuordnungen für einen bestimmten Tester zurück.
     * Ermöglicht Testern, ihre zugewiesenen Testfälle abzufragen.
     * 
     * @param testerId Die ID des Testers
     * @return Liste der Zuordnungen
     */
    @GetMapping("/tester/{testerId}")
    public ResponseEntity<List<TestCaseTestRunDTO>> getByTesterId(@PathVariable Long testerId) {
        List<TestCaseTestRunDTO> assignments = testCaseTestRunService.getByTesterId(testerId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(assignments);
    }
    
    /**
     * Erstellt eine neue Zuordnung zwischen Testfall, Testlauf und Tester.
     * Ermöglicht Testmanager:innen, Testfälle einem Testlauf zuzuweisen
     * und einen Tester für die Durchführung zu bestimmen.
     * 
     * @param dto Die Zuordnungsdaten
     * @return Die erstellte Zuordnung
     */
    @PostMapping
    public ResponseEntity<TestCaseTestRunDTO> createTestCaseTestRun(@RequestBody TestCaseTestRunDTO dto) {
        TestCaseTestRun assignment = convertToEntity(dto);
        TestCaseTestRun createdAssignment = testCaseTestRunService.createTestCaseTestRun(assignment);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(createdAssignment));
    }
    
    /**
     * Aktualisiert eine bestehende Zuordnung.
     * Ermöglicht das Ändern des zugewiesenen Testers.
     * 
     * @param id Die Zuordnungs-ID
     * @param dto Die neuen Zuordnungsdaten
     * @return Die aktualisierte Zuordnung
     */
    @PutMapping("/{id}")
    public ResponseEntity<TestCaseTestRunDTO> updateTestCaseTestRun(@PathVariable Long id, 
                                                                    @RequestBody TestCaseTestRunDTO dto) {
        TestCaseTestRun assignment = convertToEntity(dto);
        TestCaseTestRun updatedAssignment = testCaseTestRunService.updateTestCaseTestRun(id, assignment);
        return ResponseEntity.ok(convertToDTO(updatedAssignment));
    }
    
    /**
     * Löscht eine Zuordnung.
     * 
     * @param id Die Zuordnungs-ID
     * @return Leere Antwort
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTestCaseTestRun(@PathVariable Long id) {
        testCaseTestRunService.deleteTestCaseTestRun(id);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Konvertiert eine TestCaseTestRun-Entity in ein TestCaseTestRunDTO.
     * 
     * @param entity Die TestCaseTestRun-Entity
     * @return Das TestCaseTestRunDTO
     */
    private TestCaseTestRunDTO convertToDTO(TestCaseTestRun entity) {
        TestCaseTestRunDTO dto = new TestCaseTestRunDTO();
        dto.setId(entity.getId());
        dto.setTestCaseId(entity.getTestCaseId());
        dto.setTestRunId(entity.getTestRunId());
        dto.setTesterId(entity.getTesterId());
        return dto;
    }
    
    /**
     * Konvertiert ein TestCaseTestRunDTO in eine TestCaseTestRun-Entity.
     * 
     * @param dto Das TestCaseTestRunDTO
     * @return Die TestCaseTestRun-Entity
     */
    private TestCaseTestRun convertToEntity(TestCaseTestRunDTO dto) {
        TestCaseTestRun entity = new TestCaseTestRun();
        entity.setTestCaseId(dto.getTestCaseId());
        entity.setTestRunId(dto.getTestRunId());
        entity.setTesterId(dto.getTesterId());
        return entity;
    }
}
