package com.iu.require4testing.service;

import com.iu.require4testing.dto.TestRunDTO;
import com.iu.require4testing.entity.Requirement;
import com.iu.require4testing.entity.TestCaseTestRun;
import com.iu.require4testing.entity.TestRun;
import com.iu.require4testing.repository.RequirementRepository;
import com.iu.require4testing.repository.TestCaseTestRunRepository;
import com.iu.require4testing.repository.TestRunRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service-Klasse für Testlauf-Operationen.
 * Implementiert die Geschäftslogik für Testläufe und ID-Generierung.
 * Bedient sowohl REST-Controller (DTOs) als auch UI-Controller (Entities).
 *
 * <p>
 * Fachliche Anmerkung:
 * </p>
 * <ul>
 *   <li>Der Status eines Testlaufs wird beim Laden automatisch anhand der zugehörigen Testfall-Zuordnungen neu berechnet,
 *       damit die Demo-Daten (data.sql) und reine Anzeige-Views konsistente Stati zeigen.</li>
 * </ul>
 *
 * @author Require4Testing Team
 * @version 3.4.0
 */
@Service
public class TestRunService {

    private final TestRunRepository testRunRepository;
    private final RequirementRepository requirementRepository;
    private final TestCaseTestRunRepository testCaseTestRunRepository;

    /**
     * Erstellt den Service und injiziert alle benötigten Repositories.
     *
     * <p>Hinweis: Bei genau einem Konstruktor ist in Spring keine zusätzliche Annotation nötig.</p>
     *
     * @param testRunRepository Repository für Testläufe
     * @param requirementRepository Repository für Anforderungen
     * @param testCaseTestRunRepository Repository für Testfall-Zuordnungen
     */
    public TestRunService(TestRunRepository testRunRepository,
                          RequirementRepository requirementRepository,
                          TestCaseTestRunRepository testCaseTestRunRepository) {
        this.testRunRepository = testRunRepository;
        this.requirementRepository = requirementRepository;
        this.testCaseTestRunRepository = testCaseTestRunRepository;
    }

    // --- Methoden für REST Controller (DTOs) ---

    /**
     * Gibt alle Testläufe als DTOs zurück.
     *
     * <p>
     * Hinweis: Vor dem Mapping wird der Status je Testlauf gemäß der fachlichen Regeln synchronisiert.
     * </p>
     *
     * @return Liste von DTOs.
     */
    public List<TestRunDTO> getAllTestRuns() {
        return testRunRepository.findAll().stream()
                .peek(this::refreshTestRunStatusIfNeeded)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Holt einen Testlauf per ID als DTO.
     *
     * @param id Testlauf-ID.
     * @return DTO.
     */
    public TestRunDTO getTestRunById(Long id) {
        TestRun testRun = testRunRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TestRun not found"));

        refreshTestRunStatusIfNeeded(testRun);

        return convertToDTO(testRun);
    }

    /**
     * Filtert Testläufe nach Ersteller.
     *
     * @param createdBy Ersteller ID.
     * @return Liste von DTOs.
     */
    public List<TestRunDTO> getTestRunsByCreator(Long createdBy) {
        return getAllTestRuns().stream()
                .filter(tr -> tr.getCreatedBy().equals(createdBy))
                .collect(Collectors.toList());
    }

    /**
     * Filtert Testläufe nach Status.
     *
     * @param status Status String.
     * @return Liste von DTOs.
     */
    public List<TestRunDTO> getTestRunsByStatus(String status) {
        return getAllTestRuns().stream()
                .filter(tr -> tr.getStatus().equalsIgnoreCase(status))
                .collect(Collectors.toList());
    }

    /**
     * Erstellt einen neuen Testlauf aus DTO.
     *
     * @param dto DTO.
     * @return Erstelltes DTO.
     */
    public TestRunDTO createTestRun(TestRunDTO dto) {
        TestRun testRun = convertToEntity(dto);
        testRun = saveInternal(testRun);
        return convertToDTO(testRun);
    }

    /**
     * Aktualisiert Testlauf aus DTO.
     *
     * @param id ID.
     * @param dto DTO.
     * @return Aktualisiertes DTO.
     */
    public TestRunDTO updateTestRun(Long id, TestRunDTO dto) {
        TestRun existingTestRun = testRunRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TestRun not found"));

        existingTestRun.setName(dto.getName());
        existingTestRun.setDescription(dto.getDescription());

        if (dto.getStatus() != null) {
            existingTestRun.setStatus(dto.getStatus());
        }

        if (dto.getRequirementId() != null) {
            Requirement req = requirementRepository.findById(dto.getRequirementId())
                    .orElseThrow(() -> new RuntimeException("Requirement not found"));
            existingTestRun.setRequirement(req);
        }

        TestRun updatedTestRun = testRunRepository.save(existingTestRun);
        refreshTestRunStatusIfNeeded(updatedTestRun);

        return convertToDTO(updatedTestRun);
    }

    /**
     * Löscht Testlauf.
     *
     * @param id ID.
     */
    public void deleteTestRun(Long id) {
        testRunRepository.deleteById(id);
    }

    // --- Methoden für UI Controller (Entities) ---

    /**
     * Holt alle Testläufe als Entities.
     *
     * <p>
     * Vor der Rückgabe wird der Status je Testlauf gemäß der fachlichen Regeln synchronisiert,
     * damit die Übersicht sofort den korrekten Status (z.B. FAILED) anzeigt.
     * </p>
     *
     * @return Liste von Entities.
     */
    public List<TestRun> findAll() {
        List<TestRun> runs = testRunRepository.findAll();
        runs.forEach(this::refreshTestRunStatusIfNeeded);
        return runs;
    }

    /**
     * Findet Testlauf per ID als Entity.
     *
     * @param id ID.
     * @return Entity.
     */
    public TestRun findById(Long id) {
        TestRun run = testRunRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TestRun not found"));

        refreshTestRunStatusIfNeeded(run);

        return run;
    }

    /**
     * Speichert Testlauf Entity.
     *
     * @param testRun Entity.
     * @return Gespeicherte Entity.
     */
    public TestRun save(TestRun testRun) {
        return saveInternal(testRun);
    }

    private TestRun saveInternal(TestRun testRun) {
        if (testRun.getId() == null && testRun.getReadableId() == null) {
            int year = java.time.Year.now().getValue();
            long count = testRunRepository.count() + 1;
            String readableId = String.format("TR-%d-%04d", year, count);
            testRun.setReadableId(readableId);
        }

        if (testRun.getStatus() == null) {
            testRun.setStatus("PLANNING");
        }

        return testRunRepository.save(testRun);
    }

    // --- Status-Logik (zentral) ---

    /**
     * Synchronisiert den Status eines Testlaufs anhand seiner Zuordnungen (TestCaseTestRun),
     * wenn sich der fachlich erwartete Status vom gespeicherten Status unterscheidet.
     *
     * <p>
     * Regeln:
     * </p>
     * <ul>
     *   <li>{@code PLANNING}: solange keine Zuordnung existiert, die tatsächlich einem Tester zugewiesen ist.</li>
     *   <li>{@code FAILED}: sobald mindestens eine Ausführung {@code FAILED} ist.</li>
     *   <li>{@code SUCCESSFUL}: wenn mindestens eine Ausführung existiert und alle Ausführungen {@code PASSED} sind.</li>
     *   <li>{@code IN_PROGRESS}: in allen anderen Fällen (typischerweise wenn mind. eine Ausführung {@code ASSIGNED} ist).</li>
     * </ul>
     *
     * @param run Testlauf, dessen Status geprüft und ggf. aktualisiert werden soll.
     */
    private void refreshTestRunStatusIfNeeded(TestRun run) {
        List<TestCaseTestRun> assignments = testCaseTestRunRepository.findByTestRunId(run.getId());

        boolean hasAnyAssignedToTester = assignments.stream().anyMatch(a -> a.getTester() != null);
        boolean anyFailed = assignments.stream().anyMatch(a -> "FAILED".equals(a.getStatus()));
        boolean allPassed = !assignments.isEmpty() && assignments.stream().allMatch(a -> "PASSED".equals(a.getStatus()));

        String computedStatus;
        if (!hasAnyAssignedToTester) {
            computedStatus = "PLANNING";
        } else if (anyFailed) {
            computedStatus = "FAILED";
        } else if (allPassed) {
            computedStatus = "SUCCESSFUL";
        } else {
            computedStatus = "IN_PROGRESS";
        }

        if (run.getStatus() == null || !run.getStatus().equals(computedStatus)) {
            run.setStatus(computedStatus);
            testRunRepository.save(run);
        }
    }

    // --- Helper ---

    private TestRunDTO convertToDTO(TestRun testRun) {
        TestRunDTO dto = new TestRunDTO();
        dto.setId(testRun.getId());
        dto.setReadableId(testRun.getReadableId());
        dto.setName(testRun.getName());
        dto.setDescription(testRun.getDescription());
        dto.setStatus(testRun.getStatus());
        dto.setCreatedBy(testRun.getCreatedBy());
        dto.setCreatedAt(testRun.getCreatedAt());
        dto.setUpdatedAt(testRun.getUpdatedAt());
        if (testRun.getRequirement() != null) {
            dto.setRequirementId(testRun.getRequirement().getId());
        }
        return dto;
    }

    private TestRun convertToEntity(TestRunDTO dto) {
        TestRun testRun = new TestRun();
        testRun.setId(dto.getId());
        testRun.setReadableId(dto.getReadableId());
        testRun.setName(dto.getName());
        testRun.setDescription(dto.getDescription());
        testRun.setStatus(dto.getStatus());
        testRun.setCreatedBy(dto.getCreatedBy() != null ? dto.getCreatedBy() : 1L);

        if (dto.getRequirementId() != null) {
            Requirement req = requirementRepository.findById(dto.getRequirementId())
                    .orElseThrow(() -> new RuntimeException("Requirement not found"));
            testRun.setRequirement(req);
        }
        return testRun;
    }
}