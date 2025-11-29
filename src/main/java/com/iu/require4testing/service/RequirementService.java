package com.iu.require4testing.service;

import com.iu.require4testing.dto.RequirementDTO;
import com.iu.require4testing.entity.Requirement;
import com.iu.require4testing.exception.ResourceNotFoundException;
import com.iu.require4testing.repository.RequirementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service für Anforderungs-Operationen.
 * Bietet Geschäftslogik für die Verwaltung von Anforderungen.
 */
@Service
@Transactional
public class RequirementService {
    
    private final RequirementRepository requirementRepository;
    
    @Autowired
    public RequirementService(RequirementRepository requirementRepository) {
        this.requirementRepository = requirementRepository;
    }
    
    /**
     * Gibt alle Anforderungen zurück.
     * 
     * @return Liste aller Anforderungen als DTOs
     */
    public List<RequirementDTO> getAllRequirements() {
        return requirementRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Findet eine Anforderung anhand ihrer ID.
     * 
     * @param id Die Anforderungs-ID
     * @return Das Anforderungs-DTO
     * @throws ResourceNotFoundException wenn die Anforderung nicht gefunden wird
     */
    public RequirementDTO getRequirementById(Long id) {
        Requirement requirement = requirementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Anforderung", "ID", id));
        return convertToDTO(requirement);
    }
    
    /**
     * Findet Anforderungen eines bestimmten Erstellers.
     * 
     * @param createdBy Die ID des Erstellers
     * @return Liste der Anforderungen
     */
    public List<RequirementDTO> getRequirementsByCreator(Long createdBy) {
        return requirementRepository.findByCreatedBy(createdBy).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Sucht Anforderungen nach Name.
     * 
     * @param name Der Suchbegriff
     * @return Liste der gefundenen Anforderungen
     */
    public List<RequirementDTO> searchRequirementsByName(String name) {
        return requirementRepository.findByNameContainingIgnoreCase(name).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Erstellt eine neue Anforderung.
     * 
     * @param requirementDTO Die Anforderungsdaten
     * @return Das erstellte Anforderungs-DTO
     */
    public RequirementDTO createRequirement(RequirementDTO requirementDTO) {
        Requirement requirement = convertToEntity(requirementDTO);
        Requirement savedRequirement = requirementRepository.save(requirement);
        return convertToDTO(savedRequirement);
    }
    
    /**
     * Aktualisiert eine bestehende Anforderung.
     * 
     * @param id Die Anforderungs-ID
     * @param requirementDTO Die neuen Anforderungsdaten
     * @return Das aktualisierte Anforderungs-DTO
     * @throws ResourceNotFoundException wenn die Anforderung nicht gefunden wird
     */
    public RequirementDTO updateRequirement(Long id, RequirementDTO requirementDTO) {
        Requirement existingRequirement = requirementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Anforderung", "ID", id));
        
        existingRequirement.setName(requirementDTO.getName());
        existingRequirement.setDescription(requirementDTO.getDescription());
        
        Requirement updatedRequirement = requirementRepository.save(existingRequirement);
        return convertToDTO(updatedRequirement);
    }
    
    /**
     * Löscht eine Anforderung.
     * 
     * @param id Die Anforderungs-ID
     * @throws ResourceNotFoundException wenn die Anforderung nicht gefunden wird
     */
    public void deleteRequirement(Long id) {
        if (!requirementRepository.existsById(id)) {
            throw new ResourceNotFoundException("Anforderung", "ID", id);
        }
        requirementRepository.deleteById(id);
    }
    
    /**
     * Konvertiert eine Requirement-Entity in ein RequirementDTO.
     * 
     * @param requirement Die Requirement-Entity
     * @return Das RequirementDTO
     */
    private RequirementDTO convertToDTO(Requirement requirement) {
        RequirementDTO dto = new RequirementDTO();
        dto.setId(requirement.getId());
        dto.setName(requirement.getName());
        dto.setDescription(requirement.getDescription());
        dto.setCreatedBy(requirement.getCreatedBy());
        dto.setCreatedAt(requirement.getCreatedAt());
        dto.setUpdatedAt(requirement.getUpdatedAt());
        return dto;
    }
    
    /**
     * Konvertiert ein RequirementDTO in eine Requirement-Entity.
     * 
     * @param dto Das RequirementDTO
     * @return Die Requirement-Entity
     */
    private Requirement convertToEntity(RequirementDTO dto) {
        Requirement requirement = new Requirement();
        requirement.setName(dto.getName());
        requirement.setDescription(dto.getDescription());
        requirement.setCreatedBy(dto.getCreatedBy());
        return requirement;
    }
}
