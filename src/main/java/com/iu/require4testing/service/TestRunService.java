package com.iu.require4testing.service;

import com.iu.require4testing.dto.TestRunDTO;
import com.iu.require4testing.entity.TestRun;
import com.iu.require4testing.exception.ResourceNotFoundException;
import com.iu.require4testing.repository.TestRunRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service für Testlauf-Operationen.
 * Bietet Geschäftslogik für die Verwaltung von Testläufen.
 */
@Service
@Transactional
public class TestRunService {
    
    private final TestRunRepository testRunRepository;
    
    @Autowired
    public TestRunService(TestRunRepository testRunRepository) {
        this.testRunRepository = testRunRepository;
    }
    
    /**
     * Gibt alle Testläufe zurück.
     * 
     * @return Liste aller Testläufe als DTOs
     */
    public List<TestRunDTO> getAllTestRuns() {
        return testRunRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Findet einen Testlauf anhand seiner ID.
     * 
     * @param id Die Testlauf-ID
     * @return Das Testlauf-DTO
     * @throws ResourceNotFoundException wenn der Testlauf nicht gefunden wird
     */
    public TestRunDTO getTestRunById(Long id) {
        TestRun testRun = testRunRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Testlauf", "ID", id));
        return convertToDTO(testRun);
    }
    
    /**
     * Findet Testläufe eines bestimmten Erstellers.
     * 
     * @param createdBy Die ID des Erstellers
     * @return Liste der Testläufe
     */
    public List<TestRunDTO> getTestRunsByCreator(Long createdBy) {
        return testRunRepository.findByCreatedBy(createdBy).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Findet Testläufe nach Status.
     * 
     * @param status Der Status
     * @return Liste der Testläufe
     */
    public List<TestRunDTO> getTestRunsByStatus(String status) {
        return testRunRepository.findByStatus(status).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Erstellt einen neuen Testlauf.
     * 
     * @param testRunDTO Die Testlaufdaten
     * @return Das erstellte Testlauf-DTO
     */
    public TestRunDTO createTestRun(TestRunDTO testRunDTO) {
        TestRun testRun = convertToEntity(testRunDTO);
        if (testRun.getStatus() == null) {
            testRun.setStatus("PENDING");
        }
        TestRun savedTestRun = testRunRepository.save(testRun);
        return convertToDTO(savedTestRun);
    }
    
    /**
     * Aktualisiert einen bestehenden Testlauf.
     * 
     * @param id Die Testlauf-ID
     * @param testRunDTO Die neuen Testlaufdaten
     * @return Das aktualisierte Testlauf-DTO
     * @throws ResourceNotFoundException wenn der Testlauf nicht gefunden wird
     */
    public TestRunDTO updateTestRun(Long id, TestRunDTO testRunDTO) {
        TestRun existingTestRun = testRunRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Testlauf", "ID", id));
        
        existingTestRun.setName(testRunDTO.getName());
        existingTestRun.setDescription(testRunDTO.getDescription());
        existingTestRun.setStatus(testRunDTO.getStatus());
        
        TestRun updatedTestRun = testRunRepository.save(existingTestRun);
        return convertToDTO(updatedTestRun);
    }
    
    /**
     * Löscht einen Testlauf.
     * 
     * @param id Die Testlauf-ID
     * @throws ResourceNotFoundException wenn der Testlauf nicht gefunden wird
     */
    public void deleteTestRun(Long id) {
        if (!testRunRepository.existsById(id)) {
            throw new ResourceNotFoundException("Testlauf", "ID", id);
        }
        testRunRepository.deleteById(id);
    }
    
    /**
     * Konvertiert eine TestRun-Entity in ein TestRunDTO.
     * 
     * @param testRun Die TestRun-Entity
     * @return Das TestRunDTO
     */
    private TestRunDTO convertToDTO(TestRun testRun) {
        TestRunDTO dto = new TestRunDTO();
        dto.setId(testRun.getId());
        dto.setName(testRun.getName());
        dto.setDescription(testRun.getDescription());
        dto.setStatus(testRun.getStatus());
        dto.setCreatedBy(testRun.getCreatedBy());
        dto.setCreatedAt(testRun.getCreatedAt());
        dto.setUpdatedAt(testRun.getUpdatedAt());
        return dto;
    }
    
    /**
     * Konvertiert ein TestRunDTO in eine TestRun-Entity.
     * 
     * @param dto Das TestRunDTO
     * @return Die TestRun-Entity
     */
    private TestRun convertToEntity(TestRunDTO dto) {
        TestRun testRun = new TestRun();
        testRun.setName(dto.getName());
        testRun.setDescription(dto.getDescription());
        testRun.setStatus(dto.getStatus());
        testRun.setCreatedBy(dto.getCreatedBy());
        return testRun;
    }
}
