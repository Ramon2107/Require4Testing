package com.iu.require4testing.service;

import com.iu.require4testing.dto.TestCaseTestRunDTO;
import com.iu.require4testing.entity.TestCaseTestRun;
import com.iu.require4testing.repository.TestCaseTestRunRepository;
import com.iu.require4testing.repository.TestCaseRepository;
import com.iu.require4testing.repository.TestRunRepository;
import com.iu.require4testing.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service-Klasse für die Verwaltung von Testfall-Ausführungen.
 *
 * @author Require4Testing Team
 * @version 2.0.0
 */
@Service
public class TestCaseTestRunService {

    @Autowired private TestCaseTestRunRepository repo;
    @Autowired private TestCaseRepository testCaseRepo;
    @Autowired private TestRunRepository testRunRepo;
    @Autowired private UserRepository userRepo;

    // --- UI Methoden (Entity) ---

    /**
     * Findet eine Zuordnung per ID.
     * @param id ID
     * @return Entity
     */
    public TestCaseTestRun findById(Long id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Assignment not found"));
    }

    /**
     * Speichert eine Zuordnung.
     * @param assignment Entity
     * @return Entity
     */
    public TestCaseTestRun save(TestCaseTestRun assignment) {
        return repo.save(assignment);
    }

    /**
     * Lädt alle Aufgaben eines Testers als Entities.
     * Wird vom Dashboard verwendet.
     * @param testerId User ID
     * @return Liste von Entities
     */
    public List<TestCaseTestRun> findAssignmentsByTester(Long testerId) {
        return repo.findByTesterId(testerId);
    }

    // --- REST Methoden (DTO) ---

    /**
     * Lädt alle Zuordnungen als DTOs.
     * @return Liste DTOs
     */
    public List<TestCaseTestRunDTO> getAllTestCaseTestRuns() {
        return repo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    /**
     * Lädt Zuordnung per ID als DTO.
     * @param id ID
     * @return DTO
     */
    public TestCaseTestRunDTO getTestCaseTestRunById(Long id) {
        return toDTO(findById(id));
    }

    /**
     * Lädt Zuordnungen per Testfall als DTOs.
     * @param testCaseId TC ID
     * @return Liste DTOs
     */
    public List<TestCaseTestRunDTO> getByTestCaseId(Long testCaseId) {
        return repo.findAll().stream().filter(t -> t.getTestCase().getId().equals(testCaseId)).map(this::toDTO).collect(Collectors.toList());
    }

    /**
     * Lädt Zuordnungen per Lauf als DTOs.
     * @param testRunId Run ID
     * @return Liste DTOs
     */
    public List<TestCaseTestRunDTO> getByTestRunId(Long testRunId) {
        return repo.findAll().stream().filter(t -> t.getTestRun().getId().equals(testRunId)).map(this::toDTO).collect(Collectors.toList());
    }

    /**
     * Lädt Zuordnungen per Tester als DTOs.
     * @param testerId User ID
     * @return Liste DTOs
     */
    public List<TestCaseTestRunDTO> getByTesterId(Long testerId) {
        return repo.findAll().stream().filter(t -> t.getTester() != null && t.getTester().getId().equals(testerId)).map(this::toDTO).collect(Collectors.toList());
    }

    /**
     * Erstellt Zuordnung aus DTO.
     * @param dto DTO
     * @return DTO
     */
    public TestCaseTestRunDTO createFromDTO(TestCaseTestRunDTO dto) {
        TestCaseTestRun entity = new TestCaseTestRun();
        entity.setTestCase(testCaseRepo.findById(dto.getTestCaseId()).orElseThrow());
        entity.setTestRun(testRunRepo.findById(dto.getTestRunId()).orElseThrow());
        if(dto.getTesterId() != null) entity.setTester(userRepo.findById(dto.getTesterId()).orElseThrow());
        entity.setStatus(dto.getStatus());
        return toDTO(repo.save(entity));
    }

    /**
     * Aktualisiert Zuordnung aus DTO.
     * @param id ID
     * @param dto DTO
     * @return DTO
     */
    public TestCaseTestRunDTO updateTestCaseTestRun(Long id, TestCaseTestRunDTO dto) {
        TestCaseTestRun entity = findById(id);
        entity.setStatus(dto.getStatus());
        if(dto.getTesterId() != null) entity.setTester(userRepo.findById(dto.getTesterId()).orElseThrow());
        return toDTO(repo.save(entity));
    }

    /**
     * Löscht Zuordnung.
     * @param id ID
     */
    public void deleteTestCaseTestRun(Long id) {
        repo.deleteById(id);
    }

    private TestCaseTestRunDTO toDTO(TestCaseTestRun entity) {
        TestCaseTestRunDTO dto = new TestCaseTestRunDTO();
        dto.setId(entity.getId());
        dto.setTestCaseId(entity.getTestCase().getId());
        dto.setTestRunId(entity.getTestRun().getId());
        if(entity.getTester() != null) dto.setTesterId(entity.getTester().getId());
        dto.setStatus(entity.getStatus());
        return dto;
    }
}