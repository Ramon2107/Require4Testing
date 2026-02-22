package com.iu.require4testing.service;

import com.iu.require4testing.dto.TestCaseDTO;
import com.iu.require4testing.entity.TestCase;
import com.iu.require4testing.entity.TestCaseTestRun;
import com.iu.require4testing.repository.RequirementRepository;
import com.iu.require4testing.repository.TestCaseRepository;
import com.iu.require4testing.repository.TestCaseTestRunRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service-Klasse für die Verwaltung von Testfällen.
 * Beinhaltet die Geschäftslogik zur Erstellung, Änderung und Löschung von Testfällen sowie
 * die automatische Vergabe lückenloser fachlicher Identifikationsnummern im Format TC-001.
 *
 * @author Require4Testing Team
 * @version 1.6.0
 */
@Service
@Transactional
public class TestCaseService {

    /** Repository für den Zugriff auf die Testfall-Daten. */
    private final TestCaseRepository repo;
    /** Repository für den Zugriff auf die Anforderungen. */
    private final RequirementRepository reqRepo;
    /** Repository für die Zuweisungen zwischen Testfällen und Läufen. */
    private final TestCaseTestRunRepository testCaseTestRunRepository;
    /** Service zur Aktualisierung von Anforderungs-Zuständen. */
    private final RequirementService requirementService;

    /**
     * Erstellt den Service mit allen notwendigen Abhängigkeiten via Constructor Injection.
     *
     * @param repo Repository für Testfälle.
     * @param reqRepo Repository für Anforderungen.
     * @param testCaseTestRunRepository Repository für Zuweisungen.
     * @param requirementService Service für Anforderungen.
     */
    public TestCaseService(TestCaseRepository repo,
                           RequirementRepository reqRepo,
                           TestCaseTestRunRepository testCaseTestRunRepository,
                           RequirementService requirementService) {
        this.repo = repo;
        this.reqRepo = reqRepo;
        this.testCaseTestRunRepository = testCaseTestRunRepository;
        this.requirementService = requirementService;
    }

    /**
     * Findet alle im System hinterlegten Testfälle.
     * @return Eine Liste aller TestCase-Entities.
     */
    @Transactional(readOnly = true)
    public List<TestCase> findAll() {
        return repo.findAll();
    }

    /**
     * Sucht einen Testfall anhand seiner technischen ID.
     * @param id Die technische ID des Testfalls.
     * @return Das gefundene TestCase-Objekt.
     * @throws RuntimeException Falls kein Testfall unter dieser ID existiert.
     */
    @Transactional(readOnly = true)
    public TestCase findById(Long id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Testfall mit ID " + id + " wurde nicht gefunden."));
    }

    /**
     * Speichert einen Testfall in der Datenbank.
     * Falls es sich um eine Neuanlage handelt, wird automatisch die nächste TC-Nummer generiert.
     *
     * @param tc Das zu speichernde Entity-Objekt.
     * @return Das erfolgreich gespeicherte TestCase-Objekt.
     */
    public TestCase save(TestCase tc) {
        if (tc.getId() == null && tc.getReadableId() == null) {
            tc.setReadableId(generateNextReadableId());
        }
        return repo.save(tc);
    }

    /**
     * Generiert die nächste verfügbare fachliche ID im Format TC-XXX.
     * Ermittelt den höchsten aktuellen Index und zählt lückenlos hoch.
     *
     * @return Die neue fachliche ID als String (z. B. TC-016).
     */
    private String generateNextReadableId() {
        return repo.findAll().stream()
                .map(TestCase::getReadableId)
                .filter(id -> id != null && id.startsWith("TC-"))
                .map(id -> id.substring(3))
                .mapToInt(val -> {
                    try { return Integer.parseInt(val); } catch (Exception e) { return 0; }
                })
                .max()
                .stream()
                .mapToObj(max -> String.format("TC-%03d", max + 1))
                .findFirst()
                .orElse("TC-001");
    }

    /**
     * Entfernt einen Testfall und bereinigt zuvor alle bestehenden Zuordnungen in Testläufen.
     * Aktualisiert anschließend den Status der betroffenen Anforderung.
     *
     * @param testCaseId Die ID des zu löschenden Testfalls.
     */
    public void deleteTestCaseFromRequirement(Long testCaseId) {
        TestCase tc = findById(testCaseId);
        Long requirementId = tc.getRequirement() != null ? tc.getRequirement().getId() : null;

        List<TestCaseTestRun> assignments = testCaseTestRunRepository.findByTestCaseId(testCaseId);
        testCaseTestRunRepository.deleteAll(assignments);

        repo.deleteById(testCaseId);

        if (requirementId != null) {
            requirementService.refreshRequirementStatus(requirementId);
        }
    }

    /**
     * Liefert eine Liste aller Testfälle als Datentransferobjekte (DTOs).
     * @return Liste von TestCaseDTOs.
     */
    @Transactional(readOnly = true)
    public List<TestCaseDTO> getAllTestCases() {
        return repo.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    /**
     * Sucht ein einzelnes TestCaseDTO anhand der ID.
     * @param id Die technische ID.
     * @return Das gefundene DTO.
     */
    @Transactional(readOnly = true)
    public TestCaseDTO getTestCaseById(Long id) {
        return convertToDTO(findById(id));
    }

    /**
     * Sucht Testfälle anhand eines Namensfragments.
     * @param name Der Suchbegriff.
     * @return Liste passender TestCaseDTOs.
     */
    @Transactional(readOnly = true)
    public List<TestCaseDTO> searchTestCasesByName(String name) {
        return repo.findByNameContainingIgnoreCase(name).stream()
                .map(this::convertToDTO).collect(Collectors.toList());
    }

    /**
     * Liefert alle Testfälle, die zu einer bestimmten Anforderung gehören.
     * @param reqId Die ID der Anforderung.
     * @return Liste von TestCaseDTOs.
     */
    @Transactional(readOnly = true)
    public List<TestCaseDTO> getTestCasesByRequirement(Long reqId) {
        return repo.findByRequirement_Id(reqId).stream()
                .map(this::convertToDTO).collect(Collectors.toList());
    }

    /**
     * Liefert alle Testfälle zurück, die von einem spezifischen Benutzer erstellt wurden.
     * @param creatorId Die ID des Benutzers.
     * @return Liste von TestCaseDTOs.
     */
    @Transactional(readOnly = true)
    public List<TestCaseDTO> getTestCasesByCreator(Long creatorId) {
        return repo.findByCreatedBy(creatorId).stream()
                .map(this::convertToDTO).collect(Collectors.toList());
    }

    /**
     * Erstellt einen neuen Testfall basierend auf den Daten eines DTOs.
     * @param dto Das Eingabe-DTO vom Client.
     * @return Das gespeicherte TestCaseDTO inklusive generierter ID.
     */
    public TestCaseDTO createTestCase(TestCaseDTO dto) {
        TestCase entity = convertToEntity(dto);
        if (dto.getRequirementId() != null) {
            entity.setRequirement(reqRepo.findById(dto.getRequirementId()).orElseThrow());
        }
        return convertToDTO(save(entity));
    }

    /**
     * Aktualisiert einen bestehenden Testfall.
     * @param id Die ID des zu ändernden Testfalls.
     * @param dto Die neuen Daten.
     * @return Das aktualisierte TestCaseDTO.
     */
    public TestCaseDTO updateTestCase(Long id, TestCaseDTO dto) {
        TestCase existing = findById(id);
        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());
        existing.setTestSteps(dto.getTestSteps());
        if (dto.getRequirementId() != null) {
            existing.setRequirement(reqRepo.findById(dto.getRequirementId()).orElseThrow());
        }
        return convertToDTO(repo.save(existing));
    }

    /**
     * Löscht einen Testfall anhand seiner Primärschlüssel-ID.
     * @param id Die technische ID.
     */
    public void deleteTestCase(Long id) {
        repo.deleteById(id);
    }

    /**
     * Konvertiert eine TestCase Entity in ein TestCaseDTO.
     * @param entity Die Entity-Instanz.
     * @return Das befüllte DTO.
     */
    private TestCaseDTO convertToDTO(TestCase entity) {
        TestCaseDTO dto = new TestCaseDTO();
        dto.setId(entity.getId());
        dto.setReadableId(entity.getReadableId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setTestSteps(entity.getTestSteps());
        if (entity.getRequirement() != null) {
            dto.setRequirementId(entity.getRequirement().getId());
        }
        dto.setCreatedBy(entity.getCreatedBy());
        return dto;
    }

    /**
     * Konvertiert ein TestCaseDTO in eine TestCase Entity.
     * @param dto Das Quell-DTO.
     * @return Eine neue Entity-Instanz mit den Werten aus dem DTO.
     */
    private TestCase convertToEntity(TestCaseDTO dto) {
        TestCase entity = new TestCase();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setTestSteps(dto.getTestSteps());
        entity.setCreatedBy(dto.getCreatedBy() != null ? dto.getCreatedBy() : 1L);
        return entity;
    }
}