package com.iu.require4testing.controller;

import com.iu.require4testing.dto.TestCaseDTO;
import com.iu.require4testing.service.TestCaseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST-Controller für Testfall-Endpunkte.
 * Bietet CRUD-Operationen für Testfälle über die REST-API.
 * Implementiert Best Practices für RESTful Web Services.
 *
 * @author Require4Testing Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/test-cases")
public class TestCaseController {

    private final TestCaseService testCaseService;

    /**
     * Konstruktor mit Dependency Injection.
     *
     * @param testCaseService Der Testfall-Service
     */
    @Autowired
    public TestCaseController(TestCaseService testCaseService) {
        this.testCaseService = testCaseService;
    }

    /**
     * Gibt alle Testfälle zurück.
     *
     * @return Liste aller Testfälle
     */
    @GetMapping
    public ResponseEntity<List<TestCaseDTO>> getAllTestCases() {
        List<TestCaseDTO> testCases = testCaseService.getAllTestCases();
        return ResponseEntity.ok(testCases);
    }

    /**
     * Gibt einen Testfall anhand seiner ID zurück.
     *
     * @param id Die Testfall-ID
     * @return Der Testfall
     */
    @GetMapping("/{id}")
    public ResponseEntity<TestCaseDTO> getTestCaseById(@PathVariable Long id) {
        TestCaseDTO testCase = testCaseService.getTestCaseById(id);
        return ResponseEntity.ok(testCase);
    }

    /**
     * Gibt alle Testfälle einer bestimmten Anforderung zurück.
     *
     * @param requirementId Die ID der Anforderung
     * @return Liste der Testfälle
     */
    @GetMapping("/requirement/{requirementId}")
    public ResponseEntity<List<TestCaseDTO>> getTestCasesByRequirement(@PathVariable Long requirementId) {
        List<TestCaseDTO> testCases = testCaseService.getTestCasesByRequirement(requirementId);
        return ResponseEntity.ok(testCases);
    }

    /**
     * Gibt alle Testfälle eines bestimmten Erstellers zurück.
     *
     * @param createdBy Die ID des Erstellers
     * @return Liste der Testfälle
     */
    @GetMapping("/creator/{createdBy}")
    public ResponseEntity<List<TestCaseDTO>> getTestCasesByCreator(@PathVariable Long createdBy) {
        List<TestCaseDTO> testCases = testCaseService.getTestCasesByCreator(createdBy);
        return ResponseEntity.ok(testCases);
    }

    /**
     * Sucht Testfälle nach Name.
     *
     * @param name Der Suchbegriff
     * @return Liste der gefundenen Testfälle
     */
    @GetMapping("/search")
    public ResponseEntity<List<TestCaseDTO>> searchTestCases(@RequestParam String name) {
        List<TestCaseDTO> testCases = testCaseService.searchTestCasesByName(name);
        return ResponseEntity.ok(testCases);
    }

    /**
     * Erstellt einen neuen Testfall.
     * Die Eingabedaten werden automatisch validiert.
     *
     * @param testCaseDTO Die Testfalldaten (validiert)
     * @return Der erstellte Testfall
     */
    @PostMapping
    public ResponseEntity<TestCaseDTO> createTestCase(@Valid @RequestBody TestCaseDTO testCaseDTO) {
        TestCaseDTO createdTestCase = testCaseService.createTestCase(testCaseDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTestCase);
    }

    /**
     * Aktualisiert einen bestehenden Testfall.
     * Die Eingabedaten werden automatisch validiert.
     *
     * @param id Die Testfall-ID
     * @param testCaseDTO Die neuen Testfalldaten (validiert)
     * @return Der aktualisierte Testfall
     */
    @PutMapping("/{id}")
    public ResponseEntity<TestCaseDTO> updateTestCase(@PathVariable Long id,
                                                      @Valid @RequestBody TestCaseDTO testCaseDTO) {
        TestCaseDTO updatedTestCase = testCaseService.updateTestCase(id, testCaseDTO);
        return ResponseEntity.ok(updatedTestCase);
    }

    /**
     * Löscht einen Testfall.
     *
     * @param id Die Testfall-ID
     * @return Leere Antwort
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTestCase(@PathVariable Long id) {
        testCaseService.deleteTestCase(id);
        return ResponseEntity.noContent().build();
    }
}