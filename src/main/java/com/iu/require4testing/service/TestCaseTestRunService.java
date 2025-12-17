package com.iu.require4testing.service;

import com.iu.require4testing.dto.TestCaseTestRunDTO;
import com.iu.require4testing.entity.TestCaseTestRun;
import com.iu.require4testing.entity.User;
import com.iu.require4testing.repository.TestCaseRepository;
import com.iu.require4testing.repository.TestCaseTestRunRepository;
import com.iu.require4testing.repository.TestRunRepository;
import com.iu.require4testing.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Service-Klasse für die Verwaltung von Testfall-Ausführungen.
 *
 * <p>
 * Fachliche Leitplanken in dieser Version:
 * </p>
 * <ul>
 *   <li>Für Testfall-Ausführungen werden die Stati {@code ASSIGNED}, {@code PASSED} und {@code FAILED} verwendet.</li>
 *   <li>Es gibt UI-nahe Operationen zum Löschen von Zuordnungen und zum Umhängen (Reassign) auf einen anderen Tester.</li>
 * </ul>
 *
 * @author Require4Testing Team
 * @version 2.1.0
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

    private final TestCaseTestRunRepository repo;
    private final TestCaseRepository testCaseRepo;
    private final TestRunRepository testRunRepo;
    private final UserRepository userRepo;

    /**
     * Erstellt den Service und injiziert alle benötigten Repositories.
     *
     * <p>Hinweis: Bei genau einem Konstruktor ist in Spring keine zusätzliche Annotation nötig.</p>
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
     * Speichert eine Zuordnung.
     *
     * <p>
     * Diese Methode normalisiert den Statuswert, bevor gespeichert wird.
     * </p>
     *
     * @param assignment Entity
     * @return Entity
     */
    public TestCaseTestRun save(TestCaseTestRun assignment) {
        if (assignment != null && assignment.getStatus() != null) {
            assignment.setStatus(normalizeAndValidateStatus(assignment.getStatus()));
        }
        return repo.save(assignment);
    }

    /**
     * Lädt alle offenen Aufgaben eines Testers als Entities.
     * Wird vom Dashboard verwendet.
     *
     * @param testerId User ID
     * @return Liste von offenen Zuordnungen (Entities)
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
     * Weist eine bestehende Zuordnung einem anderen Tester zu (Reassign).
     *
     * <p>
     * Fachliche Logik:
     * </p>
     * <ul>
     *   <li>Der Tester wird auf den neuen Benutzer gesetzt.</li>
     *   <li>Der Status wird auf {@code ASSIGNED} gesetzt, damit die Aufgabe beim neuen Tester wieder als offen erscheint.</li>
     * </ul>
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
     * @return Liste DTOs
     */
    public List<TestCaseTestRunDTO> getAllTestCaseTestRuns() {
        return repo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    /**
     * Lädt Zuordnung per ID als DTO.
     *
     * @param id ID
     * @return DTO
     */
    public TestCaseTestRunDTO getTestCaseTestRunById(Long id) {
        return toDTO(findById(id));
    }

    /**
     * Lädt Zuordnungen per Testfall als DTOs.
     *
     * @param testCaseId TC ID
     * @return Liste DTOs
     */
    public List<TestCaseTestRunDTO> getByTestCaseId(Long testCaseId) {
        return repo.findAll().stream()
                .filter(t -> t.getTestCase().getId().equals(testCaseId))
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Lädt Zuordnungen per Lauf als DTOs.
     *
     * @param testRunId Run ID
     * @return Liste DTOs
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
     * @param testerId User ID
     * @return Liste DTOs
     */
    public List<TestCaseTestRunDTO> getByTesterId(Long testerId) {
        return repo.findAll().stream()
                .filter(t -> t.getTester() != null && t.getTester().getId().equals(testerId))
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Erstellt Zuordnung aus DTO.
     *
     * <p>
     * Wenn ein Tester gesetzt ist und kein Status übergeben wurde, wird der Status automatisch auf {@code ASSIGNED} gesetzt.
     * </p>
     *
     * @param dto DTO
     * @return DTO
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
        } else if (entity.getTester() != null) {
            entity.setStatus(STATUS_ASSIGNED);
        }

        return toDTO(repo.save(entity));
    }

    /**
     * Aktualisiert Zuordnung aus DTO.
     *
     * <p>
     * Erlaubte Stati sind ausschließlich {@code ASSIGNED}, {@code PASSED} und {@code FAILED}.
     * Zusätzlich kann der Tester geändert werden (Reassign via REST).
     * </p>
     *
     * @param id ID
     * @param dto DTO
     * @return DTO
     */
    public TestCaseTestRunDTO updateTestCaseTestRun(Long id, TestCaseTestRunDTO dto) {
        TestCaseTestRun entity = findById(id);

        if (dto.getStatus() != null) {
            entity.setStatus(normalizeAndValidateStatus(dto.getStatus()));
        }

        if (dto.getTesterId() != null) {
            entity.setTester(userRepo.findById(dto.getTesterId()).orElseThrow());
            if (entity.getStatus() == null) {
                entity.setStatus(STATUS_ASSIGNED);
            }
        }

        return toDTO(repo.save(entity));
    }

    /**
     * Löscht Zuordnung.
     *
     * @param id ID
     */
    public void deleteTestCaseTestRun(Long id) {
        repo.deleteById(id);
    }

    /**
     * Normalisiert und validiert einen Statuswert.
     *
     * <p>
     * Akzeptiert Eingaben in beliebiger Groß-/Kleinschreibung und führt sie auf die erlaubten Konstanten zurück.
     * </p>
     *
     * @param status Eingabestatus
     * @return normalisierter Status
     * @throws IllegalArgumentException wenn der Status nicht erlaubt ist
     */
    private String normalizeAndValidateStatus(String status) {
        String normalized = status.trim().toUpperCase(Locale.ROOT);

        if (STATUS_ASSIGNED.equals(normalized) || STATUS_PASSED.equals(normalized) || STATUS_FAILED.equals(normalized)) {
            return normalized;
        }

        throw new IllegalArgumentException("Ungültiger Status '" + status + "'. Erlaubt sind: ASSIGNED, PASSED, FAILED.");
    }

    private TestCaseTestRunDTO toDTO(TestCaseTestRun entity) {
        TestCaseTestRunDTO dto = new TestCaseTestRunDTO();
        dto.setId(entity.getId());
        dto.setTestCaseId(entity.getTestCase().getId());
        dto.setTestRunId(entity.getTestRun().getId());
        if (entity.getTester() != null) dto.setTesterId(entity.getTester().getId());
        dto.setStatus(entity.getStatus());
        return dto;
    }
}