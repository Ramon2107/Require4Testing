package com.iu.require4testing.service;

import com.iu.require4testing.dto.TestCaseDTO;
import com.iu.require4testing.entity.TestCase;
import com.iu.require4testing.repository.RequirementRepository;
import com.iu.require4testing.repository.TestCaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service-Klasse für die Verwaltung von Testfällen (User Story 3).
 *
 * @author Require4Testing Team
 * @version 1.0.0
 */
@Service
public class TestCaseService {

    @Autowired
    private TestCaseRepository repo;

    @Autowired
    private RequirementRepository reqRepo;

    // --- Methoden für den UI Controller (Entity-basiert) ---

    public List<TestCase> findAll() {
        return repo.findAll();
    }

    public TestCase findById(Long id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("TestCase not found with id: " + id));
    }

    public TestCase save(TestCase tc) {
        return repo.save(tc);
    }

    // --- Methoden für den REST Controller (DTO-basiert) ---

    public List<TestCaseDTO> getAllTestCases() {
        return repo.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public TestCaseDTO getTestCaseById(Long id) {
        return convertToDTO(findById(id));
    }

    public List<TestCaseDTO> searchTestCasesByName(String name) {
        return repo.findByNameContainingIgnoreCase(name).stream()
                .map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<TestCaseDTO> getTestCasesByRequirement(Long reqId) {
        return repo.findByRequirement_Id(reqId).stream()
                .map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<TestCaseDTO> getTestCasesByCreator(Long creatorId) {
        return repo.findByCreatedBy(creatorId).stream()
                .map(this::convertToDTO).collect(Collectors.toList());
    }

    public TestCaseDTO createTestCase(TestCaseDTO dto) {
        TestCase entity = convertToEntity(dto);

        if(dto.getRequirementId() != null) {
            entity.setRequirement(reqRepo.findById(dto.getRequirementId()).orElseThrow(() -> new RuntimeException("Requirement not found")));
        }

        entity = repo.save(entity);
        return convertToDTO(entity);
    }

    public TestCaseDTO updateTestCase(Long id, TestCaseDTO dto) {
        TestCase existing = findById(id);

        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());
        existing.setTestSteps(dto.getTestSteps());

        if(dto.getRequirementId() != null) {
            existing.setRequirement(reqRepo.findById(dto.getRequirementId()).orElseThrow(() -> new RuntimeException("Requirement not found")));
        }

        TestCase saved = repo.save(existing);
        return convertToDTO(saved);
    }

    public void deleteTestCase(Long id) {
        repo.deleteById(id);
    }

    // --- Hilfsmethoden für Mapping ---

    private TestCaseDTO convertToDTO(TestCase entity) {
        TestCaseDTO dto = new TestCaseDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setTestSteps(entity.getTestSteps());
        dto.setRequirementId(entity.getRequirementId());
        dto.setCreatedBy(entity.getCreatedBy());
        return dto;
    }

    private TestCase convertToEntity(TestCaseDTO dto) {
        TestCase entity = new TestCase();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setTestSteps(dto.getTestSteps());
        entity.setCreatedBy(dto.getCreatedBy() != null ? dto.getCreatedBy() : 1L); // Default User
        return entity;
    }
}