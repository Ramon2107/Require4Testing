package com.iu.require4testing.controller;

import com.iu.require4testing.dto.TestCaseTestRunDTO;
import com.iu.require4testing.service.TestCaseTestRunService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST-Controller für die Verwaltung von Testfall-Ausführungen (TestCaseTestRuns).
 * <p>
 * Dieser Controller ermöglicht den Zugriff auf die Zwischentabelle, die Testfälle mit Testläufen verbindet.
 * Er erlaubt das Abrufen, Erstellen, Aktualisieren und Löschen von Testausführungen sowie
 * deren Statusverwaltung.
 * </p>
 *
 * @author Require4Testing Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/test-case-test-runs")
public class TestCaseTestRunController {

    /**
     * Der Service für die Geschäftslogik der Testausführungen.
     */
    @Autowired
    private TestCaseTestRunService testCaseTestRunService;

    /**
     * Ruft alle Testfall-Ausführungen ab.
     *
     * @return Eine Liste aller TestCaseTestRunDTOs.
     */
    @GetMapping
    public ResponseEntity<List<TestCaseTestRunDTO>> getAllTestCaseTestRuns() {
        return ResponseEntity.ok(testCaseTestRunService.getAllTestCaseTestRuns());
    }

    /**
     * Ruft eine spezifische Testfall-Ausführung anhand ihrer ID ab.
     *
     * @param id Die ID der gesuchten Ausführung.
     * @return Das gefundene DTO.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TestCaseTestRunDTO> getTestCaseTestRunById(@PathVariable Long id) {
        return ResponseEntity.ok(testCaseTestRunService.getTestCaseTestRunById(id));
    }

    /**
     * Findet alle Ausführungen für einen bestimmten Testfall.
     *
     * @param id Die ID des Testfalls.
     * @return Eine Liste von DTOs.
     */
    @GetMapping("/test-case/{id}")
    public ResponseEntity<List<TestCaseTestRunDTO>> getByTestCaseId(@PathVariable Long id) {
        return ResponseEntity.ok(testCaseTestRunService.getByTestCaseId(id));
    }

    /**
     * Findet alle Testfall-Ausführungen innerhalb eines bestimmten Testlaufs.
     *
     * @param id Die ID des Testlaufs.
     * @return Eine Liste von DTOs.
     */
    @GetMapping("/test-run/{id}")
    public ResponseEntity<List<TestCaseTestRunDTO>> getByTestRunId(@PathVariable Long id) {
        return ResponseEntity.ok(testCaseTestRunService.getByTestRunId(id));
    }

    /**
     * Findet alle Ausführungen, die einem bestimmten Tester zugewiesen sind.
     *
     * @param id Die ID des Testers.
     * @return Eine Liste von DTOs.
     */
    @GetMapping("/tester/{id}")
    public ResponseEntity<List<TestCaseTestRunDTO>> getByTesterId(@PathVariable Long id) {
        return ResponseEntity.ok(testCaseTestRunService.getByTesterId(id));
    }

    /**
     * Erstellt eine neue Testfall-Ausführung (weist einen Testfall einem Testlauf zu).
     *
     * @param dto Die Daten der neuen Zuordnung.
     * @return Das erstellte DTO.
     */
    @PostMapping
    public ResponseEntity<TestCaseTestRunDTO> create(@RequestBody TestCaseTestRunDTO dto) {
        return ResponseEntity.ok(testCaseTestRunService.createFromDTO(dto));
    }

    /**
     * Aktualisiert eine bestehende Testfall-Ausführung (z.B. Statusänderung).
     *
     * @param id Die ID der zu aktualisierenden Ausführung.
     * @param dto Die neuen Daten.
     * @return Das aktualisierte DTO.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TestCaseTestRunDTO> update(@PathVariable Long id, @RequestBody TestCaseTestRunDTO dto) {
        return ResponseEntity.ok(testCaseTestRunService.updateTestCaseTestRun(id, dto));
    }

    /**
     * Löscht eine Testfall-Ausführung (entfernt den Testfall aus dem Lauf).
     *
     * @param id Die ID der zu löschenden Ausführung.
     * @return Eine leere Antwort.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        testCaseTestRunService.deleteTestCaseTestRun(id);
        return ResponseEntity.noContent().build();
    }
}