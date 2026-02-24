package com.iu.require4testing.controller;

import com.iu.require4testing.dto.TestCaseTestRunDTO;
import com.iu.require4testing.service.TestCaseTestRunService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST-Controller für die Verwaltung von Testfall-Ausführungen (TestCaseTestRuns).
 *
 * <p>
 * Dieser Controller ermöglicht den Zugriff auf die Zwischentabelle, die Testfälle mit Testläufen verbindet.
 * Er erlaubt das Abrufen, Erstellen, Aktualisieren und Löschen von Testausführungen sowie deren Statusverwaltung.
 * </p>
 *
 * <p>
 * Technischer Hinweis:
 * Die Abhängigkeit wird per Constructor Injection eingebunden, um Feldinjektion zu vermeiden.
 * </p>
 *
 * @author Require4Testing Team
 * @version 1.1.0
 */
@RestController
@RequestMapping("/api/test-case-test-runs")
public class TestCaseTestRunController {

    private final TestCaseTestRunService testCaseTestRunService;

    /**
     * Erstellt den Controller mit dem benötigten Service.
     *
     * @param testCaseTestRunService Service für Testfall-Ausführungen
     */
    public TestCaseTestRunController(TestCaseTestRunService testCaseTestRunService) {
        this.testCaseTestRunService = testCaseTestRunService;
    }

    @GetMapping
    public ResponseEntity<List<TestCaseTestRunDTO>> getAllTestCaseTestRuns() {
        return ResponseEntity.ok(testCaseTestRunService.getAllTestCaseTestRuns());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TestCaseTestRunDTO> getTestCaseTestRunById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(testCaseTestRunService.getTestCaseTestRunById(id));
    }

    @GetMapping("/test-case/{id}")
    public ResponseEntity<List<TestCaseTestRunDTO>> getByTestCaseId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(testCaseTestRunService.getByTestCaseId(id));
    }

    @GetMapping("/test-run/{id}")
    public ResponseEntity<List<TestCaseTestRunDTO>> getByTestRunId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(testCaseTestRunService.getByTestRunId(id));
    }

    @GetMapping("/tester/{id}")
    public ResponseEntity<List<TestCaseTestRunDTO>> getByTesterId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(testCaseTestRunService.getByTesterId(id));
    }

    @PostMapping
    public ResponseEntity<TestCaseTestRunDTO> create(@RequestBody TestCaseTestRunDTO dto) {
        return ResponseEntity.ok(testCaseTestRunService.createFromDTO(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TestCaseTestRunDTO> update(@PathVariable("id") Long id, @RequestBody TestCaseTestRunDTO dto) {
        return ResponseEntity.ok(testCaseTestRunService.updateTestCaseTestRun(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        testCaseTestRunService.deleteTestCaseTestRun(id);
        return ResponseEntity.noContent().build();
    }
}