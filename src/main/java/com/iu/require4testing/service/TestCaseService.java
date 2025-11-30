package com.iu.require4testing.service;

import com.iu.require4testing.dto.TestCaseDTO;
import com.iu.require4testing.entity.TestCase;
import com.iu.require4testing.exception.ResourceNotFoundException;
import com.iu.require4testing.repository.TestCaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service für Testfall-Operationen.
 * Bietet Geschäftslogik für die Verwaltung von Testfällen.
 */
@Service
@Transactional
public class TestCaseService {
    
    private final TestCaseRepository testCaseRepository;
    
    @Autowired
    public TestCaseService(TestCaseRepository testCaseRepository) {
        this.testCaseRepository = testCaseRepository;
    }
    
    /**
     * Gibt alle Testfälle zurück.
     * 
     * @return Liste aller Testfälle als DTOs
     */
    public List<TestCaseDTO> getAllTestCases() {
        return testCaseRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Findet einen Testfall anhand seiner ID.
     * 
     * @param id Die Testfall-ID
     * @return Das Testfall-DTO
     * @throws ResourceNotFoundException wenn der Testfall nicht gefunden wird
     */
    public TestCaseDTO getTestCaseById(Long id) {
        TestCase testCase = testCaseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Testfall", "ID", id));
        return convertToDTO(testCase);
    }
    
    /**
     * Findet Testfälle einer bestimmten Anforderung.
     * 
     * @param requirementId Die ID der Anforderung
     * @return Liste der Testfälle
     */
    public List<TestCaseDTO> getTestCasesByRequirement(Long requirementId) {
        return testCaseRepository.findByRequirementId(requirementId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Findet Testfälle eines bestimmten Erstellers.
     * 
     * @param createdBy Die ID des Erstellers
     * @return Liste der Testfälle
     */
    public List<TestCaseDTO> getTestCasesByCreator(Long createdBy) {
        return testCaseRepository.findByCreatedBy(createdBy).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Sucht Testfälle nach Name.
     * 
     * @param name Der Suchbegriff
     * @return Liste der gefundenen Testfälle
     */
    public List<TestCaseDTO> searchTestCasesByName(String name) {
        return testCaseRepository.findByNameContainingIgnoreCase(name).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Erstellt einen neuen Testfall.
     * 
     * @param testCaseDTO Die Testfalldaten
     * @return Das erstellte Testfall-DTO
     */
    public TestCaseDTO createTestCase(TestCaseDTO testCaseDTO) {
        TestCase testCase = convertToEntity(testCaseDTO);
        TestCase savedTestCase = testCaseRepository.save(testCase);
        return convertToDTO(savedTestCase);
    }
    
    /**
     * Aktualisiert einen bestehenden Testfall.
     * 
     * @param id Die Testfall-ID
     * @param testCaseDTO Die neuen Testfalldaten
     * @return Das aktualisierte Testfall-DTO
     * @throws ResourceNotFoundException wenn der Testfall nicht gefunden wird
     */
    public TestCaseDTO updateTestCase(Long id, TestCaseDTO testCaseDTO) {
        TestCase existingTestCase = testCaseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Testfall", "ID", id));
        
        existingTestCase.setName(testCaseDTO.getName());
        existingTestCase.setDescription(testCaseDTO.getDescription());
        existingTestCase.setTestSteps(testCaseDTO.getTestSteps());
        existingTestCase.setRequirementId(testCaseDTO.getRequirementId());
        
        TestCase updatedTestCase = testCaseRepository.save(existingTestCase);
        return convertToDTO(updatedTestCase);
    }
    
    /**
     * Löscht einen Testfall.
     * 
     * @param id Die Testfall-ID
     * @throws ResourceNotFoundException wenn der Testfall nicht gefunden wird
     */
    public void deleteTestCase(Long id) {
        if (!testCaseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Testfall", "ID", id);
        }
        testCaseRepository.deleteById(id);
    }
    
    /**
     * Konvertiert eine TestCase-Entity in ein TestCaseDTO.
     * 
     * @param testCase Die TestCase-Entity
     * @return Das TestCaseDTO
     */
    private TestCaseDTO convertToDTO(TestCase testCase) {
        TestCaseDTO dto = new TestCaseDTO();
        dto.setId(testCase.getId());
        dto.setName(testCase.getName());
        dto.setDescription(testCase.getDescription());
        dto.setTestSteps(testCase.getTestSteps());
        dto.setRequirementId(testCase.getRequirementId());
        dto.setCreatedBy(testCase.getCreatedBy());
        dto.setCreatedAt(testCase.getCreatedAt());
        dto.setUpdatedAt(testCase.getUpdatedAt());
        return dto;
    }
    
    /**
     * Konvertiert ein TestCaseDTO in eine TestCase-Entity.
     * 
     * @param dto Das TestCaseDTO
     * @return Die TestCase-Entity
     */
    private TestCase convertToEntity(TestCaseDTO dto) {
        TestCase testCase = new TestCase();
        testCase.setName(dto.getName());
        testCase.setDescription(dto.getDescription());
        testCase.setTestSteps(dto.getTestSteps());
        testCase.setRequirementId(dto.getRequirementId());
        testCase.setCreatedBy(dto.getCreatedBy());
        return testCase;
    }
}
