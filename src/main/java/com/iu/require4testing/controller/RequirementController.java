package com.iu.require4testing.controller;

import com.iu.require4testing.dto.RequirementDTO;
import com.iu.require4testing.service.RequirementService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST-Controller für Anforderungs-Endpunkte.
 * Bietet CRUD-Operationen für Anforderungen über die REST-API.
 * Implementiert Best Practices für RESTful Web Services.
 */
@RestController
@RequestMapping("/api/requirements")
public class RequirementController {

    private final RequirementService requirementService;

    /**
     * Erstellt den Controller und injiziert den {@link RequirementService}.
     *
     * <p>Hinweis: Bei genau einem Konstruktor ist in Spring keine zusätzliche Annotation nötig.</p>
     *
     * @param requirementService Der Anforderungs-Service.
     */
    public RequirementController(RequirementService requirementService) {
        this.requirementService = requirementService;
    }

    /**
     * Gibt alle Anforderungen zurück.
     *
     * @return Liste aller Anforderungen
     */
    @GetMapping
    public ResponseEntity<List<RequirementDTO>> getAllRequirements() {
        List<RequirementDTO> requirements = requirementService.getAllRequirements();
        return ResponseEntity.ok(requirements);
    }

    /**
     * Gibt eine Anforderung anhand ihrer ID zurück.
     *
     * @param id Die Anforderungs-ID
     * @return Die Anforderung
     */
    @GetMapping("/{id}")
    public ResponseEntity<RequirementDTO> getRequirementById(@PathVariable Long id) {
        RequirementDTO requirement = requirementService.getRequirementById(id);
        return ResponseEntity.ok(requirement);
    }

    /**
     * Gibt alle Anforderungen eines bestimmten Erstellers zurück.
     *
     * @param createdBy Die ID des Erstellers
     * @return Liste der Anforderungen
     */
    @GetMapping("/creator/{createdBy}")
    public ResponseEntity<List<RequirementDTO>> getRequirementsByCreator(@PathVariable Long createdBy) {
        List<RequirementDTO> requirements = requirementService.getRequirementsByCreator(createdBy);
        return ResponseEntity.ok(requirements);
    }

    /**
     * Sucht Anforderungen nach Name.
     *
     * @param name Der Suchbegriff
     * @return Liste der gefundenen Anforderungen
     */
    @GetMapping("/search")
    public ResponseEntity<List<RequirementDTO>> searchRequirements(@RequestParam String name) {
        List<RequirementDTO> requirements = requirementService.searchRequirementsByName(name);
        return ResponseEntity.ok(requirements);
    }

    /**
     * Erstellt eine neue Anforderung.
     * Die Eingabedaten werden automatisch validiert.
     *
     * @param requirementDTO Die Anforderungsdaten (validiert)
     * @return Die erstellte Anforderung
     */
    @PostMapping
    public ResponseEntity<RequirementDTO> createRequirement(@Valid @RequestBody RequirementDTO requirementDTO) {
        RequirementDTO createdRequirement = requirementService.createRequirement(requirementDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRequirement);
    }

    /**
     * Aktualisiert eine bestehende Anforderung.
     * Die Eingabedaten werden automatisch validiert.
     *
     * @param id Die Anforderungs-ID
     * @param requirementDTO Die neuen Anforderungsdaten (validiert)
     * @return Die aktualisierte Anforderung
     */
    @PutMapping("/{id}")
    public ResponseEntity<RequirementDTO> updateRequirement(@PathVariable Long id,
                                                            @Valid @RequestBody RequirementDTO requirementDTO) {
        RequirementDTO updatedRequirement = requirementService.updateRequirement(id, requirementDTO);
        return ResponseEntity.ok(updatedRequirement);
    }

    /**
     * Löscht eine Anforderung.
     *
     * @param id Die Anforderungs-ID
     * @return Leere Antwort
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRequirement(@PathVariable Long id) {
        requirementService.deleteRequirement(id);
        return ResponseEntity.noContent().build();
    }
}