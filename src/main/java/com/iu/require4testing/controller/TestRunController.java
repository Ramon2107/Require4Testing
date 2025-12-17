package com.iu.require4testing.controller;

import com.iu.require4testing.dto.TestRunDTO;
import com.iu.require4testing.service.TestRunService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST-Controller für die Ressource "TestRun" (Testlauf).
 * <p>
 * Dieser Controller stellt die HTTP-Endpunkte für CRUD-Operationen (Create, Read, Update, Delete)
 * auf Testläufen bereit. Er nimmt Anfragen entgegen, validiert diese und delegiert die Verarbeitung
 * an den {@link TestRunService}.
 * </p>
 *
 * @author Require4Testing Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/test-runs")
public class TestRunController {

    /**
     * Der Service für die Geschäftslogik von Testläufen.
     */
    private final TestRunService testRunService;

    /**
     * Erstellt den Controller und injiziert den {@link TestRunService}.
     *
     * <p>Hinweis: Bei genau einem Konstruktor ist in Spring keine zusätzliche Annotation nötig.</p>
     *
     * @param testRunService Die zu injizierende Service-Instanz.
     */
    public TestRunController(TestRunService testRunService) {
        this.testRunService = testRunService;
    }

    /**
     * Ruft alle verfügbaren Testläufe ab.
     *
     * @return Eine Liste von {@link TestRunDTO} Objekten, verpackt in einer {@link ResponseEntity} mit Status 200.
     */
    @GetMapping
    public ResponseEntity<List<TestRunDTO>> getAllTestRuns() {
        List<TestRunDTO> testRuns = testRunService.getAllTestRuns();
        return ResponseEntity.ok(testRuns);
    }

    /**
     * Ruft einen spezifischen Testlauf anhand seiner ID ab.
     *
     * @param id Die ID des gesuchten Testlaufs.
     * @return Das gefundene {@link TestRunDTO} Objekt oder ein Fehlerstatus.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TestRunDTO> getTestRunById(@PathVariable Long id) {
        TestRunDTO testRun = testRunService.getTestRunById(id);
        return ResponseEntity.ok(testRun);
    }

    /**
     * Sucht alle Testläufe, die von einem bestimmten Benutzer erstellt wurden.
     *
     * @param createdBy Die ID des erstellenden Benutzers.
     * @return Eine Liste der gefundenen {@link TestRunDTO} Objekte.
     */
    @GetMapping("/creator/{createdBy}")
    public ResponseEntity<List<TestRunDTO>> getTestRunsByCreator(@PathVariable Long createdBy) {
        List<TestRunDTO> testRuns = testRunService.getTestRunsByCreator(createdBy);
        return ResponseEntity.ok(testRuns);
    }

    /**
     * Sucht alle Testläufe, die einen bestimmten Status haben.
     *
     * @param status Der Status-String (z.B. "PLANNING", "IN_PROGRESS").
     * @return Eine Liste der gefundenen {@link TestRunDTO} Objekte.
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<TestRunDTO>> getTestRunsByStatus(@PathVariable String status) {
        List<TestRunDTO> testRuns = testRunService.getTestRunsByStatus(status);
        return ResponseEntity.ok(testRuns);
    }

    /**
     * Erstellt einen neuen Testlauf.
     * Die Eingabedaten werden mittels {@code @Valid} gegen die Bean-Validation-Regeln geprüft.
     *
     * @param testRunDTO Das DTO mit den Daten für den neuen Testlauf.
     * @return Das erstellte {@link TestRunDTO} inklusive generierter ID, mit Status 201 (Created).
     */
    @PostMapping
    public ResponseEntity<TestRunDTO> createTestRun(@Valid @RequestBody TestRunDTO testRunDTO) {
        TestRunDTO createdTestRun = testRunService.createTestRun(testRunDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTestRun);
    }

    /**
     * Aktualisiert einen bestehenden Testlauf.
     *
     * @param id Die ID des zu aktualisierenden Testlaufs.
     * @param testRunDTO Das DTO mit den neuen Daten.
     * @return Das aktualisierte {@link TestRunDTO} mit Status 200 (OK).
     */
    @PutMapping("/{id}")
    public ResponseEntity<TestRunDTO> updateTestRun(@PathVariable Long id,
                                                    @Valid @RequestBody TestRunDTO testRunDTO) {
        TestRunDTO updatedTestRun = testRunService.updateTestRun(id, testRunDTO);
        return ResponseEntity.ok(updatedTestRun);
    }

    /**
     * Löscht einen Testlauf anhand seiner ID.
     *
     * @param id Die ID des zu löschenden Testlaufs.
     * @return Eine leere Antwort mit Status 204 (No Content).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTestRun(@PathVariable Long id) {
        testRunService.deleteTestRun(id);
        return ResponseEntity.noContent().build();
    }
}