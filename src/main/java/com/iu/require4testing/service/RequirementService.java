package com.iu.require4testing.service;

import com.iu.require4testing.dto.RequirementDTO;
import com.iu.require4testing.entity.Requirement;
import com.iu.require4testing.entity.TestRun;
import com.iu.require4testing.repository.RequirementRepository;
import com.iu.require4testing.repository.TestCaseRepository;
import com.iu.require4testing.repository.TestCaseTestRunRepository;
import com.iu.require4testing.repository.TestRunRepository;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service für die Verwaltung von Anforderungen.
 * Beinhaltet die Geschäftslogik zur Statusberechnung und Datenhaltung.
 * Bedient sowohl REST-Controller (DTOs) als auch UI-Controller (Entities).
 *
 * <p>
 * Statusregeln (fachlich):
 * </p>
 * <ul>
 *   <li>Eine Anforderung bleibt in {@code PLANNING}, solange noch kein einziger Testfall zur Anforderung definiert ist.</li>
 *   <li>Wenn keine Testläufe existieren, bleibt die Anforderung ebenfalls {@code PLANNING}.</li>
 *   <li>Wenn in irgendeinem zugehörigen Testlauf mindestens ein Testfall {@code FAILED} ist, muss die Anforderung {@code FAILED} sein.</li>
 *   <li>Wenn kein Failed, aber mindestens eine zugewiesene Ausführung nicht {@code PASSED} ist, ist die Anforderung {@code IN_PROGRESS}.</li>
 *   <li>Wenn es zugewiesene Ausführungen gibt und alle {@code PASSED} sind, ist die Anforderung {@code SUCCESSFUL}.</li>
 * </ul>
 *
 * <p>
 * Performance:
 * Für die Statusberechnung werden gezielte {@code exists...}-Abfragen verwendet, um N+1-Loads zu vermeiden.
 * </p>
 *
 * <p>
 * Technischer Hinweis:
 * Die Abhängigkeiten werden per Constructor Injection eingebunden (statt Feldinjektion).
 * </p>
 *
 * @author Require4Testing Team
 * @version 3.6.1
 */
@Service
public class RequirementService {

    private final RequirementRepository repo;
    private final TestRunRepository testRunRepo;
    private final TestCaseRepository testCaseRepo;
    private final TestCaseTestRunRepository testCaseTestRunRepo;

    /**
     * Erstellt den Service mit allen notwendigen Repositories.
     *
     * @param repo Repository für Anforderungen
     * @param testRunRepo Repository für Testläufe
     * @param testCaseRepo Repository für Testfälle
     * @param testCaseTestRunRepo Repository für Testfall-Zuordnungen
     */
    public RequirementService(RequirementRepository repo,
                              TestRunRepository testRunRepo,
                              TestCaseRepository testCaseRepo,
                              TestCaseTestRunRepository testCaseTestRunRepo) {
        this.repo = repo;
        this.testRunRepo = testRunRepo;
        this.testCaseRepo = testCaseRepo;
        this.testCaseTestRunRepo = testCaseTestRunRepo;
    }

    // --- Methoden für UI Controller (Entities) ---

    public List<Requirement> findAll() {
        List<Requirement> reqs = repo.findAll();
        reqs.forEach(this::updateStatusBasedOnRules);
        return reqs;
    }

    public Requirement findById(Long id) {
        Requirement req = repo.findById(id).orElseThrow(() -> new RuntimeException("Requirement not found"));
        updateStatusBasedOnRules(req);
        return req;
    }

    public Requirement save(Requirement requirement) {
        if (requirement.getId() == null && requirement.getReadableId() == null) {
            int year = Year.now().getValue();
            long count = repo.count() + 1;
            String readableId = String.format("REQ-%d-%04d", year, count);
            requirement.setReadableId(readableId);
        }

        if (requirement.getStatus() == null || requirement.getStatus().isEmpty()) {
            requirement.setStatus("PLANNING");
        }

        return repo.save(requirement);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    public void refreshRequirementStatus(Long requirementId) {
        Requirement req = repo.findById(requirementId).orElse(null);
        if (req != null) {
            updateStatusBasedOnRules(req);
            repo.save(req);
        }
    }

    // --- Interne Logik ---

    private void updateStatusBasedOnRules(Requirement req) {
        boolean hasAnyTestCase = !testCaseRepo.findByRequirement_Id(req.getId()).isEmpty();
        if (!hasAnyTestCase) {
            req.setStatus("PLANNING");
            return;
        }

        List<TestRun> testRuns = testRunRepo.findByRequirementId(req.getId());
        if (testRuns.isEmpty()) {
            req.setStatus("PLANNING");
            return;
        }

        boolean anyFailed = testCaseTestRunRepo.existsByTestRun_Requirement_IdAndStatus(req.getId(), "FAILED");
        if (anyFailed) {
            req.setStatus("FAILED");
            return;
        }

        boolean hasAnyAssignedToTester = testCaseTestRunRepo.existsByTestRun_Requirement_IdAndTesterIsNotNull(req.getId());
        if (!hasAnyAssignedToTester) {
            req.setStatus("PLANNING");
            return;
        }

        boolean anyNotPassedButAssigned = testCaseTestRunRepo.existsByTestRun_Requirement_IdAndTesterIsNotNullAndStatusNot(req.getId(), "PASSED");
        if (anyNotPassedButAssigned) {
            req.setStatus("IN_PROGRESS");
            return;
        }

        req.setStatus("SUCCESSFUL");
    }

    // --- Methoden für REST Controller (DTOs) ---

    public List<RequirementDTO> getAllRequirements() {
        return findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public RequirementDTO getRequirementById(Long id) {
        return convertToDTO(findById(id));
    }

    public List<RequirementDTO> getRequirementsByCreator(Long createdBy) {
        return repo.findAll().stream()
                .filter(req -> req.getCreatedBy().equals(createdBy))
                .peek(this::updateStatusBasedOnRules)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<RequirementDTO> searchRequirementsByName(String name) {
        return repo.findAll().stream()
                .filter(req -> req.getName().toLowerCase().contains(name.toLowerCase()))
                .peek(this::updateStatusBasedOnRules)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public RequirementDTO createRequirement(RequirementDTO dto) {
        Requirement req = convertToEntity(dto);
        req = save(req);
        return convertToDTO(req);
    }

    public RequirementDTO updateRequirement(Long id, RequirementDTO dto) {
        Requirement existing = findById(id);
        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());
        if (dto.getStatus() != null) existing.setStatus(dto.getStatus());
        return convertToDTO(repo.save(existing));
    }

    public void deleteRequirement(Long id) {
        delete(id);
    }

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