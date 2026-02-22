package com.iu.require4testing.service;

import com.iu.require4testing.dto.TestCaseTestRunDTO;
import com.iu.require4testing.entity.TestCaseTestRun;
import com.iu.require4testing.entity.User;
import com.iu.require4testing.repository.TestCaseRepository;
import com.iu.require4testing.repository.TestCaseTestRunRepository;
import com.iu.require4testing.repository.TestRunRepository;
import com.iu.require4testing.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Service-Klasse für die Verwaltung von Testfall-Ausführungen.
 *
 * <p>
 * Das Testergebnis wird direkt in der Ausführung gespeichert {@link TestCaseTestRun}
 * Dadurch sind Ergebnis (PASSED/FAILED) und optionale Notizen
 * eindeutig an die konkrete Kombination aus Testfall und Testlauf gebunden.
 * </p>
 *
 * <p>
 * Für Testfall-Ausführungen werden die Stati {@code ASSIGNED}, {@code PASSED} und {@code FAILED} verwendet.
 * </p>
 *
 * @author Require4Testing Team
 * @version 2.4.0
 */
@Service
public class TestCaseTestRunService {

    /**
     * Status-Konstante für eine zugewiesene (noch nicht abgeschlossene) Aufgabe.
     */
    public static final String STATUS_ASSIGNED = "ASSIGNED";

    /**
     * Status-Konstante für eine erfolgreich abgeschlossene Ausführung.
     */
    public static final String STATUS_PASSED = "PASSED";

    /**
     * Status-Konstante für eine fehlgeschlagene Ausführung.
     */
    public static final String STATUS_FAILED = "FAILED";

    /** Repository für die Zuordnungs-Entität. */
    private final TestCaseTestRunRepository repo;
    /** Repository für Testfälle. */
    private final TestCaseRepository testCaseRepo;
    /** Repository für Testläufe. */
    private final TestRunRepository testRunRepo;
    /** Repository für Benutzer. */
    private final UserRepository userRepo;

    /**
     * Erstellt den Service und injiziert alle benötigten Repositories via Constructor Injection.
     *
     * @param repo Repository für Zuordnungen (Testfall ↔ Testlauf)
     * @param testCaseRepo Repository für Testfälle
     * @param testRunRepo Repository für Testläufe
     * @param userRepo Repository für Benutzer
     */
    public TestCaseTestRunService(TestCaseTestRunRepository repo,
                                  TestCaseRepository testCaseRepo,
                                  TestRunRepository testRunRepo,
                                  UserRepository userRepo) {
        this.repo = repo;
        this.testCaseRepo = testCaseRepo;
        this.testRunRepo = testRunRepo;
        this.userRepo = userRepo;
    }

    // --- UI Methoden (Entity) ---

    /**
     * Findet eine Zuordnung per ID.
     *
     * @param id ID der Zuordnung
     * @return Entity
     * @throws RuntimeException wenn die Zuordnung nicht existiert
     */
    public TestCaseTestRun findById(Long id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Assignment not found"));
    }

    /**
     * Speichert eine Zuordnung und setzt automatisch Metadaten.
     *
     * <p>
     * Diese Methode normalisiert den Statuswert. Wenn der Status
     * auf {@code PASSED} oder {@code FAILED} gesetzt wird und noch kein Ausführungszeitpunkt
     * vorhanden ist, wird dieser auf {@link LocalDateTime#now()} gesetzt.
     * </p>
     *
     * @param assignment Die zu speichernde Zuordnungs-Entity.
     * @return Die gespeicherte Entity.
     * @throws IllegalArgumentException falls das Assignment null ist.
     */
    public TestCaseTestRun save(TestCaseTestRun assignment) {
        if (assignment == null) {
            throw new IllegalArgumentException("assignment darf nicht null sein");
        }

        if (assignment.getStatus() != null) {
            assignment.setStatus(normalizeAndValidateStatus(assignment.getStatus()));
        } else if (assignment.getTester() != null) {
            assignment.setStatus(STATUS_ASSIGNED);
        }

        // Setze Ausführungszeitpunkt, wenn Ergebnis vorliegt und noch kein Zeitstempel existiert
        if ((STATUS_PASSED.equals(assignment.getStatus()) || STATUS_FAILED.equals(assignment.getStatus()))
                && assignment.getExecutedAt() == null) {
            assignment.setExecutedAt(LocalDateTime.now());
        }

        return repo.save(assignment);
    }

    /**
     * Prüft fachlich, ob ein Testfall bereits einem Testlauf zugeordnet wurde.
     * Dient der Vermeidung von Dubletten innerhalb eines Laufs.
     *
     * @param runId ID des Testlaufs
     * @param testCaseId ID des Testfalls
     * @return true, falls die Zuordnung bereits existiert.
     */
    public boolean existsAssignment(Long runId, Long testCaseId) {
        return repo.findAll().stream()
                .anyMatch(a -> a.getTestRun().getId().equals(runId) && a.getTestCase().getId().equals(testCaseId));
    }

    /**
     * Lädt alle offenen Aufgaben eines Testers als Entities.
     * Wird vom Dashboard verwendet.
     *
     * @param testerId Die ID des Testers.
     * @return Liste von offenen Zuordnungen (Status ASSIGNED).
     */
    public List<TestCaseTestRun> findAssignmentsByTester(Long testerId) {
        return repo.findByTesterIdAndStatus(testerId, STATUS_ASSIGNED);
    }

    /**
     * Löscht eine Zuordnung (entfernt einen Testfall aus einem Testlauf).
     *
     * @param assignmentId ID der Zuordnung
     * @throws RuntimeException wenn die Zuordnung nicht existiert
     */
    public void deleteAssignment(Long assignmentId) {
        TestCaseTestRun existing = findById(assignmentId);
        repo.deleteById(existing.getId());
    }

    /**
     * Weist eine bestehende Zuordnung einem anderen Tester zu.
     *
     * @param assignmentId ID der Zuordnung
     * @param newTesterId ID des neuen Testers
     * @return die aktualisierte Zuordnung
     * @throws RuntimeException wenn Zuordnung oder Benutzer nicht existieren
     */
    public TestCaseTestRun reassignTester(Long assignmentId, Long newTesterId) {
        TestCaseTestRun existing = findById(assignmentId);
        User newTester = userRepo.findById(newTesterId).orElseThrow(() -> new RuntimeException("User not found"));

        existing.setTester(newTester);
        existing.setStatus(STATUS_ASSIGNED);

        return repo.save(existing);
    }

    // --- REST Methoden (DTO) ---

    /**
     * Lädt alle Zuordnungen als DTOs.
     *
     * @return Liste von DTOs.
     */
    public List<TestCaseTestRunDTO> getAllTestCaseTestRuns() {
        return repo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    /**
     * Lädt eine Zuordnung per ID als DTO.
     *
     * @param id Die ID der Zuordnung.
     * @return Das entsprechende DTO.
     */
    public TestCaseTestRunDTO getTestCaseTestRunById(Long id) {
        return toDTO(findById(id));
    }

    /**
     * Lädt Zuordnungen per Testfall als DTOs.
     *
     * @param testCaseId ID des Testfalls.
     * @return Liste von DTOs.
     */
    public List<TestCaseTestRunDTO> getByTestCaseId(Long testCaseId) {
        return repo.findAll().stream()
                .filter(t -> t.getTestCase().getId().equals(testCaseId))
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Lädt Zuordnungen per Testlauf als DTOs.
     *
     * @param testRunId ID des Testlaufs.
     * @return Liste von DTOs.
     */
    public List<TestCaseTestRunDTO> getByTestRunId(Long testRunId) {
        return repo.findAll().stream()
                .filter(t -> t.getTestRun().getId().equals(testRunId))
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Lädt Zuordnungen per Tester als DTOs.
     *
     * @param testerId ID des Benutzers.
     * @return Liste von DTOs.
     */
    public List<TestCaseTestRunDTO> getByTesterId(Long testerId) {
        return repo.findAll().stream()
                .filter(t -> t.getTester() != null && t.getTester().getId().equals(testerId))
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Erstellt eine Zuordnung basierend auf einem DTO.
     *
     * @param dto Das Datenübertragungsobjekt.
     * @return Das gespeicherte DTO.
     */
    public TestCaseTestRunDTO createFromDTO(TestCaseTestRunDTO dto) {
        TestCaseTestRun entity = new TestCaseTestRun();
        entity.setTestCase(testCaseRepo.findById(dto.getTestCaseId()).orElseThrow());
        entity.setTestRun(testRunRepo.findById(dto.getTestRunId()).orElseThrow());

        if (dto.getTesterId() != null) {
            entity.setTester(userRepo.findById(dto.getTesterId()).orElseThrow());
        }

        if (dto.getStatus() != null) {
            entity.setStatus(normalizeAndValidateStatus(dto.getStatus()));
        } else {
            entity.setStatus(STATUS_ASSIGNED);
        }

        entity.setNotes(dto.getNotes());
        entity.setExecutedAt(dto.getExecutedAt());

        if ((STATUS_PASSED.equals(entity.getStatus()) || STATUS_FAILED.equals(entity.getStatus()))
                && entity.getExecutedAt() == null) {
            entity.setExecutedAt(LocalDateTime.now());
        }

        return toDTO(repo.save(entity));
    }

    /**
     * Aktualisiert eine Zuordnung basierend auf einem DTO.
     *
     * @param id Die ID der Zuordnung.
     * @param dto Das DTO mit den neuen Daten.
     * @return Das aktualisierte DTO.
     */
    public TestCaseTestRunDTO updateTestCaseTestRun(Long id, TestCaseTestRunDTO dto) {
        TestCaseTestRun entity = findById(id);

        if (dto.getStatus() != null) {
            entity.setStatus(normalizeAndValidateStatus(dto.getStatus()));
        }

        if (dto.getTesterId() != null) {
            entity.setTester(userRepo.findById(dto.getTesterId()).orElseThrow());
        }

        if (dto.getNotes() != null) {
            entity.setNotes(dto.getNotes());
        }

        if (dto.getExecutedAt() != null) {
            entity.setExecutedAt(dto.getExecutedAt());
        }

        if ((STATUS_PASSED.equals(entity.getStatus()) || STATUS_FAILED.equals(entity.getStatus()))
                && entity.getExecutedAt() == null) {
            entity.setExecutedAt(LocalDateTime.now());
        }

        return toDTO(repo.save(entity));
    }

    /**
     * Löscht eine Zuordnung per ID.
     *
     * @param id Die ID der zu löschenden Zuordnung.
     */
    public void deleteTestCaseTestRun(Long id) {
        repo.deleteById(id);
    }

    /**
     * Normalisiert und validiert einen Statuswert.
     *
     * @param status Eingabestatus.
     * @return Normalisierter Status (Großbuchstaben).
     * @throws IllegalArgumentException falls der Status ungültig ist.
     */
    private String normalizeAndValidateStatus(String status) {
        String normalized = status.trim().toUpperCase(Locale.ROOT);

        if (STATUS_ASSIGNED.equals(normalized) || STATUS_PASSED.equals(normalized) || STATUS_FAILED.equals(normalized)) {
            return normalized;
        }

        throw new IllegalArgumentException("Ungültiger Status '" + status + "'. Erlaubt sind: ASSIGNED, PASSED, FAILED.");
    }

    /**
     * Transformiert eine Entity in ein DTO.
     *
     * @param entity Die Entity.
     * @return Das DTO.
     */
    private TestCaseTestRunDTO toDTO(TestCaseTestRun entity) {
        TestCaseTestRunDTO dto = new TestCaseTestRunDTO();
        dto.setId(entity.getId());
        dto.setTestCaseId(entity.getTestCase().getId());
        dto.setTestRunId(entity.getTestRun().getId());
        if (entity.getTester() != null) {
            dto.setTesterId(entity.getTester().getId());
        }
        dto.setStatus(entity.getStatus());
        dto.setNotes(entity.getNotes());
        dto.setExecutedAt(entity.getExecutedAt());
        return dto;
    }
}