package com.iu.require4testing.service;

import com.iu.require4testing.dto.TestResultDTO;
import com.iu.require4testing.entity.TestResult;
import com.iu.require4testing.exception.ResourceNotFoundException;
import com.iu.require4testing.repository.TestResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service für Testergebnis-Operationen.
 * Bietet Geschäftslogik für die Verwaltung von Testergebnissen.
 */
@Service
@Transactional
public class TestResultService {
    
    private final TestResultRepository testResultRepository;
    
    @Autowired
    public TestResultService(TestResultRepository testResultRepository) {
        this.testResultRepository = testResultRepository;
    }
    
    /**
     * Gibt alle Testergebnisse zurück.
     * 
     * @return Liste aller Testergebnisse als DTOs
     */
    public List<TestResultDTO> getAllTestResults() {
        return testResultRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Findet ein Testergebnis anhand seiner ID.
     * 
     * @param id Die Testergebnis-ID
     * @return Das Testergebnis-DTO
     * @throws ResourceNotFoundException wenn das Testergebnis nicht gefunden wird
     */
    public TestResultDTO getTestResultById(Long id) {
        TestResult testResult = testResultRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Testergebnis", "ID", id));
        return convertToDTO(testResult);
    }
    
    /**
     * Findet Testergebnisse eines bestimmten Testfalls.
     * 
     * @param testCaseId Die ID des Testfalls
     * @return Liste der Testergebnisse
     */
    public List<TestResultDTO> getTestResultsByTestCase(Long testCaseId) {
        return testResultRepository.findByTestCaseId(testCaseId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Findet Testergebnisse eines bestimmten Testlaufs.
     * 
     * @param testRunId Die ID des Testlaufs
     * @return Liste der Testergebnisse
     */
    public List<TestResultDTO> getTestResultsByTestRun(Long testRunId) {
        return testResultRepository.findByTestRunId(testRunId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Findet Testergebnisse eines bestimmten Testers.
     * 
     * @param testerId Die ID des Testers
     * @return Liste der Testergebnisse
     */
    public List<TestResultDTO> getTestResultsByTester(Long testerId) {
        return testResultRepository.findByTesterId(testerId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Erstellt ein neues Testergebnis.
     * 
     * @param testResultDTO Die Testergebnisdaten
     * @return Das erstellte Testergebnis-DTO
     */
    public TestResultDTO createTestResult(TestResultDTO testResultDTO) {
        TestResult testResult = convertToEntity(testResultDTO);
        if (testResult.getExecutedAt() == null) {
            testResult.setExecutedAt(LocalDateTime.now());
        }
        TestResult savedTestResult = testResultRepository.save(testResult);
        return convertToDTO(savedTestResult);
    }
    
    /**
     * Aktualisiert ein bestehendes Testergebnis.
     * 
     * @param id Die Testergebnis-ID
     * @param testResultDTO Die neuen Testergebnisdaten
     * @return Das aktualisierte Testergebnis-DTO
     * @throws ResourceNotFoundException wenn das Testergebnis nicht gefunden wird
     */
    public TestResultDTO updateTestResult(Long id, TestResultDTO testResultDTO) {
        TestResult existingTestResult = testResultRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Testergebnis", "ID", id));
        
        existingTestResult.setStatus(testResultDTO.getStatus());
        existingTestResult.setNotes(testResultDTO.getNotes());
        if (testResultDTO.getExecutedAt() != null) {
            existingTestResult.setExecutedAt(testResultDTO.getExecutedAt());
        }
        
        TestResult updatedTestResult = testResultRepository.save(existingTestResult);
        return convertToDTO(updatedTestResult);
    }
    
    /**
     * Löscht ein Testergebnis.
     * 
     * @param id Die Testergebnis-ID
     * @throws ResourceNotFoundException wenn das Testergebnis nicht gefunden wird
     */
    public void deleteTestResult(Long id) {
        if (!testResultRepository.existsById(id)) {
            throw new ResourceNotFoundException("Testergebnis", "ID", id);
        }
        testResultRepository.deleteById(id);
    }
    
    /**
     * Konvertiert eine TestResult-Entity in ein TestResultDTO.
     * 
     * @param testResult Die TestResult-Entity
     * @return Das TestResultDTO
     */
    private TestResultDTO convertToDTO(TestResult testResult) {
        TestResultDTO dto = new TestResultDTO();
        dto.setId(testResult.getId());
        dto.setTestCaseId(testResult.getTestCaseId());
        dto.setTestRunId(testResult.getTestRunId());
        dto.setTesterId(testResult.getTesterId());
        dto.setStatus(testResult.getStatus());
        dto.setNotes(testResult.getNotes());
        dto.setExecutedAt(testResult.getExecutedAt());
        dto.setCreatedAt(testResult.getCreatedAt());
        dto.setUpdatedAt(testResult.getUpdatedAt());
        return dto;
    }
    
    /**
     * Konvertiert ein TestResultDTO in eine TestResult-Entity.
     * 
     * @param dto Das TestResultDTO
     * @return Die TestResult-Entity
     */
    private TestResult convertToEntity(TestResultDTO dto) {
        TestResult testResult = new TestResult();
        testResult.setTestCaseId(dto.getTestCaseId());
        testResult.setTestRunId(dto.getTestRunId());
        testResult.setTesterId(dto.getTesterId());
        testResult.setStatus(dto.getStatus());
        testResult.setNotes(dto.getNotes());
        testResult.setExecutedAt(dto.getExecutedAt());
        return testResult;
    }
}
