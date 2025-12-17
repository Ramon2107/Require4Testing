package com.iu.require4testing.service;

import com.iu.require4testing.dto.RequirementDTO;
import com.iu.require4testing.entity.Requirement;
import com.iu.require4testing.entity.TestRun;
import com.iu.require4testing.repository.RequirementRepository;
import com.iu.require4testing.repository.TestRunRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service für die Verwaltung von Anforderungen.
 * Beinhaltet die Geschäftslogik zur Statusberechnung und Datenhaltung.
 * Bedient sowohl REST-Controller (DTOs) als auch UI-Controller (Entities).
 *
 * @author Require4Testing Team
 * @version 3.3.0
 */
@Service
public class RequirementService {

    @Autowired
    private RequirementRepository repo;

    @Autowired
    private TestRunRepository testRunRepo;

    // --- Methoden für UI Controller (Entities) ---

    /**
     * Findet alle Anforderungen und aktualisiert deren Status basierend auf den Testläufen.
     *
     * @return Liste aller Anforderungen (Entities).
     */
    public List<Requirement> findAll() {
        List<Requirement> reqs = repo.findAll();
        reqs.forEach(this::updateStatusBasedOnTestRuns);
        return reqs;
    }

    /**
     * Findet eine Anforderung per ID und aktualisiert den Status.
     *
     * @param id Die ID der Anforderung.
     * @return Die gefundene Anforderung.
     */
    public Requirement findById(Long id) {
        Requirement req = repo.findById(id).orElseThrow(() -> new RuntimeException("Requirement not found"));
        updateStatusBasedOnTestRuns(req);
        return req;
    }

    /**
     * Speichert eine Anforderung.
     * Generiert beim ersten Speichern eine sprechende ID (REQ-YYYY-NNNN).
     *
     * @param requirement Die zu speichernde Anforderung.
     * @return Die gespeicherte Anforderung.
     */
    public Requirement save(Requirement requirement) {
        // ID Generierung wenn neu (REQ-YYYY-NNNN)
        if (requirement.getId() == null && requirement.getReadableId() == null) {
            int year = Year.now().getValue();
            long count = repo.count() + 1; // Start bei 0001
            String readableId = String.format("REQ-%d-%04d", year, count);
            requirement.setReadableId(readableId);
        }

        // Initialer Status immer PLANNED
        if (requirement.getStatus() == null || requirement.getStatus().isEmpty()) {
            requirement.setStatus("PLANNED");
        }

        return repo.save(requirement);
    }

    /**
     * Löscht eine Anforderung.
     *
     * @param id Die ID der zu löschenden Anforderung.
     */
    public void delete(Long id) {
        repo.deleteById(id);
    }

    /**
     * Erzwingt eine Neuberechnung und Speicherung des Status für eine Anforderung.
     * Wird vom UiController aufgerufen, wenn sich Testläufe ändern.
     *
     * @param requirementId Die ID der Anforderung.
     */
    public void refreshRequirementStatus(Long requirementId) {
        Requirement req = repo.findById(requirementId).orElse(null);
        if (req != null) {
            updateStatusBasedOnTestRuns(req);
            repo.save(req);
        }
    }

    // --- Interne Logik ---

    /**
     * Berechnet den Status der Anforderung basierend auf den verknüpften Testläufen.
     * Logik:
     * 1. Wenn KEINE Testläufe -> PLANNED
     * 2. Wenn IRGENDEIN Testlauf FAILED ist -> FAILED (egal was sonst passiert)
     * 3. Wenn KEIN Failed, aber mind. einer IN_PROGRESS oder PLANNING -> IN_PROGRESS
     * 4. Wenn alle Testläufe durch sind (SUCCESSFUL/COMPLETED) und keiner Failed -> SUCCESSFUL
     *
     * @param req Die zu prüfende Anforderung.
     */
    private void updateStatusBasedOnTestRuns(Requirement req) {
        List<TestRun> testRuns = testRunRepo.findByRequirementId(req.getId());

        if (testRuns.isEmpty()) {
            req.setStatus("PLANNED");
            return;
        }

        boolean anyFailed = false;
        boolean anyInProgress = false;

        // Checke alle Runs
        for (TestRun tr : testRuns) {
            String s = tr.getStatus();
            if ("FAILED".equals(s)) {
                anyFailed = true;
            } else if ("IN_PROGRESS".equals(s) || "PLANNING".equals(s)) {
                anyInProgress = true;
            }
        }

        if (anyFailed) {
            req.setStatus("FAILED");
        } else if (anyInProgress) {
            req.setStatus("IN_PROGRESS");
        } else {
            req.setStatus("SUCCESSFUL");
        }
    }

    // --- Methoden für REST Controller (DTOs) ---

    /**
     * Gibt alle Anforderungen als DTOs zurück.
     * @return Liste von DTOs.
     */
    public List<RequirementDTO> getAllRequirements() {
        return findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Gibt eine Anforderung per ID als DTO zurück.
     * @param id ID der Anforderung.
     * @return DTO.
     */
    public RequirementDTO getRequirementById(Long id) {
        return convertToDTO(findById(id));
    }

    /**
     * Sucht Anforderungen eines Erstellers.
     * @param createdBy User ID.
     * @return Liste von DTOs.
     */
    public List<RequirementDTO> getRequirementsByCreator(Long createdBy) {
        return repo.findAll().stream()
                .filter(req -> req.getCreatedBy().equals(createdBy))
                .peek(this::updateStatusBasedOnTestRuns)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Sucht Anforderungen nach Name.
     * @param name Suchbegriff.
     * @return Liste von DTOs.
     */
    public List<RequirementDTO> searchRequirementsByName(String name) {
        return repo.findAll().stream()
                .filter(req -> req.getName().toLowerCase().contains(name.toLowerCase()))
                .peek(this::updateStatusBasedOnTestRuns)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Erstellt Anforderung aus DTO.
     * @param dto DTO.
     * @return Erstelltes DTO.
     */
    public RequirementDTO createRequirement(RequirementDTO dto) {
        Requirement req = convertToEntity(dto);
        req = save(req);
        return convertToDTO(req);
    }

    /**
     * Aktualisiert Anforderung aus DTO.
     * @param id ID.
     * @param dto DTO.
     * @return Aktualisiertes DTO.
     */
    public RequirementDTO updateRequirement(Long id, RequirementDTO dto) {
        Requirement existing = findById(id);
        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());
        if(dto.getStatus() != null) existing.setStatus(dto.getStatus());
        return convertToDTO(repo.save(existing));
    }

    /**
     * Löscht Anforderung (REST).
     * @param id ID.
     */
    public void deleteRequirement(Long id) {
        delete(id);
    }

    // Helper
    private RequirementDTO convertToDTO(Requirement req) {
        RequirementDTO dto = new RequirementDTO();
        dto.setId(req.getId());
        dto.setReadableId(req.getReadableId());
        dto.setName(req.getName());
        dto.setDescription(req.getDescription());
        dto.setStatus(req.getStatus());
        dto.setCreatedBy(req.getCreatedBy());
        dto.setCreatedAt(req.getCreatedAt());
        dto.setUpdatedAt(req.getUpdatedAt());
        return dto;
    }

    private Requirement convertToEntity(RequirementDTO dto) {
        Requirement req = new Requirement();
        req.setId(dto.getId());
        req.setReadableId(dto.getReadableId());
        req.setName(dto.getName());
        req.setDescription(dto.getDescription());
        req.setStatus(dto.getStatus());
        req.setCreatedBy(dto.getCreatedBy());
        return req;
    }
}