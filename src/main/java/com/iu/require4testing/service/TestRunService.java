package com.iu.require4testing.service;

import com.iu.require4testing.dto.TestRunDTO;
import com.iu.require4testing.entity.Requirement;
import com.iu.require4testing.entity.TestRun;
import com.iu.require4testing.repository.RequirementRepository;
import com.iu.require4testing.repository.TestRunRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service-Klasse für Testlauf-Operationen.
 * Implementiert die Geschäftslogik für Testläufe und ID-Generierung.
 * Bedient sowohl REST-Controller (DTOs) als auch UI-Controller (Entities).
 *
 * @author Require4Testing Team
 * @version 3.3.0
 */
@Service
public class TestRunService {

    @Autowired
    private TestRunRepository testRunRepository;

    @Autowired
    private RequirementRepository requirementRepository;

    // --- Methoden für REST Controller (DTOs) ---

    /**
     * Gibt alle Testläufe als DTOs zurück.
     * @return Liste von DTOs.
     */
    public List<TestRunDTO> getAllTestRuns() {
        return testRunRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Holt einen Testlauf per ID als DTO.
     * @param id Testlauf-ID.
     * @return DTO.
     */
    public TestRunDTO getTestRunById(Long id) {
        TestRun testRun = testRunRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TestRun not found"));
        return convertToDTO(testRun);
    }

    /**
     * Filtert Testläufe nach Ersteller.
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
        return convertToDTO(updatedTestRun);
    }

    /**
     * Löscht Testlauf.
     * @param id ID.
     */
    public void deleteTestRun(Long id) {
        testRunRepository.deleteById(id);
    }

    // --- Methoden für UI Controller (Entities) ---

    /**
     * Holt alle Testläufe als Entities.
     * @return Liste von Entities.
     */
    public List<TestRun> findAll() {
        return testRunRepository.findAll();
    }

    /**
     * Findet Testlauf per ID als Entity.
     * @param id ID.
     * @return Entity.
     */
    public TestRun findById(Long id) {
        return testRunRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TestRun not found"));
    }

    /**
     * Speichert Testlauf Entity.
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