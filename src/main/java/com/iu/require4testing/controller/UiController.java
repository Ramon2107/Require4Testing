package com.iu.require4testing.controller;

import com.iu.require4testing.entity.Requirement;
import com.iu.require4testing.entity.TestCase;
import com.iu.require4testing.entity.TestCaseTestRun;
import com.iu.require4testing.entity.TestRun;
import com.iu.require4testing.entity.User;
import com.iu.require4testing.service.RequirementService;
import com.iu.require4testing.service.TestCaseService;
import com.iu.require4testing.service.TestCaseTestRunService;
import com.iu.require4testing.service.TestRunService;
import com.iu.require4testing.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Haupt-Controller für die Web-Oberfläche (UI).
 * Setzt das Rollenkonzept strikt um und verwaltet die Workflows.
 *
 * <p>
 * Fachliche Regeln:
 * </p>
 * <ul>
 *   <li>Anforderungen bleiben in {@code PLANNING}, solange keine Testfälle existieren (Statusberechnung im {@link RequirementService}).</li>
 *   <li>Testläufe bleiben in {@code PLANNING}, solange keine Testfall-Zuordnung einem Tester zugewiesen ist.</li>
 *   <li>Für Testausführungen werden die Stati {@code ASSIGNED}, {@code PASSED} und {@code FAILED} verwendet.</li>
 *   <li>Testfälle können aus einer Anforderung entfernt werden (Testfall wird gelöscht, inkl. Bereinigung der Testlauf-Zuordnungen).</li>
 *   <li>Testfälle können aus einem Testlauf entfernt werden (Zuordnung {@link TestCaseTestRun} wird gelöscht).</li>
 *   <li>Zuordnungen können auf einen anderen Tester umgehängt werden (Reassign), der Status wird dabei auf {@code ASSIGNED} gesetzt.</li>
 * </ul>
 *
 * <p>
 * Rollenbezeichnungen:
 * Admin, Tester, Testfallersteller, Testmanager, Requirements Engineer.
 * </p>
 *
 * <p>
 * Technischer Hinweis:
 * Die Abhängigkeiten werden per Constructor Injection eingebunden. Dadurch werden Feldinjektionen vermieden,
 * die Klasse wird testbarer und Abhängigkeiten werden explizit.
 * </p>
 *
 * @author Require4Testing Team
 * @version 3.8.0
 */
@Controller
public class UiController {

    private final RequirementService requirementService;
    private final TestCaseService testCaseService;
    private final TestRunService testRunService;
    private final TestCaseTestRunService testCaseTestRunService;
    private final UserService userService;

    /**
     * Erstellt den UI-Controller mit allen benötigten Services.
     *
     * @param requirementService Service für Anforderungen
     * @param testCaseService Service für Testfälle
     * @param testRunService Service für Testläufe
     * @param testCaseTestRunService Service für Zuordnungen Testfall↔Testlauf
     * @param userService Service für Benutzer
     */
    public UiController(RequirementService requirementService,
                        TestCaseService testCaseService,
                        TestRunService testRunService,
                        TestCaseTestRunService testCaseTestRunService,
                        UserService userService) {
        this.requirementService = requirementService;
        this.testCaseService = testCaseService;
        this.testRunService = testRunService;
        this.testCaseTestRunService = testCaseTestRunService;
        this.userService = userService;
    }

    // --- SESSION & AUTH ---

    /**
     * Liefert für Browser-Anfragen nach einem Favicon bewusst eine leere Antwort.
     *
     * <p>
     * Hintergrund: Browser fordern häufig automatisch {@code /favicon.ico} an.
     * Wenn keine Datei vorhanden ist, soll dies nicht zu einem 500-Fehler führen.
     * Ein 204 (No Content) ist für den Browser ausreichend und verhindert unnötige Fehlermeldungen.
     * </p>
     *
     * @return 204 No Content.
     */
    @GetMapping("/favicon.ico")
    public ResponseEntity<Void> favicon() {
        return ResponseEntity.noContent().build();
    }

    /**
     * Zeigt die Login-Seite an.
     *
     * @param model Model zur Übergabe der verfügbaren Demo-Benutzer.
     * @return Name des Login-Templates.
     */
    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("users", userService.findAll());
        return "login";
    }

    /**
     * Führt die Login-Logik aus (Demo-Modus).
     *
     * <p>
     * Der ausgewählte Benutzer wird in der HTTP-Session als {@code currentUser} hinterlegt.
     * </p>
     *
     * @param userId ID des ausgewählten Benutzers.
     * @param session HTTP-Session.
     * @return Redirect auf das Dashboard.
     */
    @PostMapping("/login")
    public String login(@RequestParam Long userId, HttpSession session) {
        session.setAttribute("currentUser", userService.findById(userId));
        return "redirect:/";
    }

    /**
     * Führt die Logout-Logik aus (Session invalidieren).
     *
     * @param session HTTP-Session.
     * @return Redirect auf die Login-Seite.
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    /**
     * Ermittelt den aktuell angemeldeten Benutzer aus der Session.
     *
     * @param session HTTP-Session.
     * @return Aktueller Benutzer oder {@code null}.
     */
    private User getCurrentUser(HttpSession session) {
        return (User) session.getAttribute("currentUser");
    }

    // --- DASHBOARD ---

    /**
     * Zeigt das Dashboard.
     *
     * <p>
     * Für Tester werden nur offene Aufgaben (Status {@code ASSIGNED}) angezeigt.
     * </p>
     *
     * @param model Model.
     * @param session HTTP-Session.
     * @return Dashboard-Template oder Redirect auf Login.
     */
    @GetMapping("/")
    public String index(Model model, HttpSession session) {
        User user = getCurrentUser(session);
        if (user == null) return "redirect:/login";

        model.addAttribute("user", user);
        if ("TESTER".equals(user.getRole())) {
            model.addAttribute("myTasks", testCaseTestRunService.findAssignmentsByTester(user.getId()));
        }
        return "index";
    }

    // --- ANFORDERUNGEN ---

    /**
     * Listet alle Anforderungen.
     *
     * @param model Model.
     * @param session HTTP-Session.
     * @return Anforderungen-Template oder Redirect auf Login.
     */
    @GetMapping("/ui/requirements")
    public String listRequirements(Model model, HttpSession session) {
        if (getCurrentUser(session) == null) return "redirect:/login";
        model.addAttribute("requirements", requirementService.findAll());
        model.addAttribute("newRequirement", new Requirement());
        return "requirements";
    }

    /**
     * Erstellt eine neue Anforderung.
     *
     * <p>
     * Berechtigung: Nur Requirements Engineer und Admin.
     * </p>
     *
     * @param requirement Requirement-Entity aus dem Formular.
     * @param session HTTP-Session.
     * @param redirectAttributes Flash-Messages.
     * @return Redirect auf die Anforderungen-Liste.
     */
    @PostMapping("/ui/requirements")
    public String createRequirement(@ModelAttribute Requirement requirement,
                                    HttpSession session,
                                    RedirectAttributes redirectAttributes) {
        User user = getCurrentUser(session);
        if (user == null) return "redirect:/login";

        boolean allowed = "REQUIREMENT_ENGINEER".equals(user.getRole()) || "ADMIN".equals(user.getRole());
        if (!allowed) {
            redirectAttributes.addFlashAttribute("errorMessage", "Fehlende Berechtigung: Nur 'Requirements Engineer' oder 'Admin' darf Anforderungen erstellen.");
            return "redirect:/ui/requirements";
        }

        requirement.setCreatedBy(user.getId());
        requirementService.save(requirement);
        redirectAttributes.addFlashAttribute("successMessage", "Anforderung erstellt.");
        return "redirect:/ui/requirements";
    }

    // --- TESTFÄLLE ---

    /**
     * Zeigt die Detailseite einer Anforderung inkl. ihrer Testfälle.
     *
     * @param id Requirement-ID.
     * @param model Model.
     * @param session HTTP-Session.
     * @return Requirement-Detailtemplate oder Redirect auf Login.
     */
    @GetMapping("/ui/requirements/{id}")
    public String requirementDetails(@PathVariable Long id, Model model, HttpSession session) {
        if (getCurrentUser(session) == null) return "redirect:/login";

        Requirement req = requirementService.findById(id);
        model.addAttribute("requirement", req);

        TestCase newTestCase = new TestCase();
        newTestCase.setRequirement(req);
        model.addAttribute("newTestCase", newTestCase);

        return "requirement_details";
    }

    /**
     * Erstellt einen Testfall für eine Anforderung.
     *
     * <p>
     * Fachliche Regel: Sobald mindestens ein Testfall existiert, darf die Anforderung nicht mehr
     * in {@code PLANNING} verbleiben. Daher wird nach dem Speichern der Requirement-Status
     * explizit neu berechnet.
     * </p>
     *
     * <p>
     * Berechtigung: Nur Testfallersteller (CREATOR) und Admin.
     * </p>
     *
     * @param id Requirement-ID.
     * @param testCase Neuer Testfall aus dem Formular.
     * @param session HTTP-Session.
     * @param redirectAttributes Flash-Messages.
     * @return Redirect auf die Requirement-Detailseite.
     */
    @PostMapping("/ui/requirements/{id}/testcases")
    public String createTestCase(@PathVariable Long id,
                                 @ModelAttribute TestCase testCase,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        User user = getCurrentUser(session);
        if (user == null) return "redirect:/login";

        boolean allowed = "CREATOR".equals(user.getRole()) || "ADMIN".equals(user.getRole());
        if (!allowed) {
            redirectAttributes.addFlashAttribute("errorMessage", "Fehlende Berechtigung: Nur 'Testfallersteller' oder 'Admin' darf Testfälle erstellen.");
            return "redirect:/ui/requirements/" + id;
        }

        Requirement req = requirementService.findById(id);
        testCase.setRequirement(req);
        testCase.setCreatedBy(user.getId());
        testCase.setId(null);

        testCaseService.save(testCase);
        requirementService.refreshRequirementStatus(id);

        redirectAttributes.addFlashAttribute("successMessage", "Testfall erstellt.");
        return "redirect:/ui/requirements/" + id;
    }

    /**
     * Löscht einen Testfall aus einer Anforderung.
     *
     * <p>
     * Fachliche Bedeutung: „Aus Anforderung entfernen“ heißt, der Testfall (Stammdaten) wird gelöscht.
     * Dabei werden automatisch auch alle Zuordnungen des Testfalls zu Testläufen entfernt und der
     * Requirement-Status neu berechnet.
     * </p>
     *
     * <p>
     * Berechtigung: Testfallersteller (CREATOR) oder Admin.
     * </p>
     *
     * @param reqId Requirement-ID (für Redirect im UI-Kontext).
     * @param tcId TestCase-ID.
     * @param session HTTP-Session.
     * @param redirectAttributes Flash-Messages.
     * @return Redirect zurück auf die Requirement-Detailseite.
     */
    @PostMapping("/ui/requirements/{reqId}/testcases/{tcId}/delete")
    public String deleteTestCaseFromRequirement(@PathVariable Long reqId,
                                                @PathVariable Long tcId,
                                                HttpSession session,
                                                RedirectAttributes redirectAttributes) {
        User user = getCurrentUser(session);
        if (user == null) return "redirect:/login";

        boolean allowed = "CREATOR".equals(user.getRole()) || "ADMIN".equals(user.getRole());
        if (!allowed) {
            redirectAttributes.addFlashAttribute("errorMessage", "Fehlende Berechtigung: Nur 'Testfallersteller' oder 'Admin' darf Testfälle löschen.");
            return "redirect:/ui/requirements/" + reqId;
        }

        testCaseService.deleteTestCaseFromRequirement(tcId);

        redirectAttributes.addFlashAttribute("successMessage", "Testfall wurde aus der Anforderung entfernt.");
        return "redirect:/ui/requirements/" + reqId;
    }

    // --- TESTLÄUFE ---

    /**
     * Listet alle Testläufe.
     *
     * @param model Model.
     * @param session HTTP-Session.
     * @return Testläufe-Template oder Redirect auf Login.
     */
    @GetMapping("/ui/testruns")
    public String listTestRuns(Model model, HttpSession session) {
        if (getCurrentUser(session) == null) return "redirect:/login";
        model.addAttribute("testRuns", testRunService.findAll());
        model.addAttribute("requirements", requirementService.findAll());
        model.addAttribute("newTestRun", new TestRun());
        return "testruns";
    }

    /**
     * Erstellt einen Testlauf.
     *
     * <p>
     * Berechtigung: Nur Testmanager (MANAGER) und Admin.
     * </p>
     *
     * @param testRun Testlauf aus dem Formular.
     * @param requirementId Zugehörige Requirement-ID.
     * @param session HTTP-Session.
     * @param redirectAttributes Flash-Messages.
     * @return Redirect auf die Testläufe-Liste.
     */
    @PostMapping("/ui/testruns")
    public String createTestRun(@ModelAttribute TestRun testRun,
                                @RequestParam Long requirementId,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        User user = getCurrentUser(session);
        if (user == null) return "redirect:/login";

        boolean allowed = "MANAGER".equals(user.getRole()) || "ADMIN".equals(user.getRole());
        if (!allowed) {
            redirectAttributes.addFlashAttribute("errorMessage", "Fehlende Berechtigung: Nur 'Testmanager' oder 'Admin' darf Testläufe erstellen.");
            return "redirect:/ui/testruns";
        }

        testRun.setCreatedBy(user.getId());
        testRun.setStatus("PLANNING");
        testRun.setRequirement(requirementService.findById(requirementId));

        testRunService.save(testRun);
        requirementService.refreshRequirementStatus(requirementId);

        redirectAttributes.addFlashAttribute("successMessage", "Testlauf geplant.");
        return "redirect:/ui/testruns";
    }

    // --- TESTDURCHFÜHRUNG / TESTLAUF-DETAILS ---

    /**
     * Zeigt die Detailseite eines Testlaufs inkl. Zuweisungen.
     *
     * @param id Testlauf-ID.
     * @param model Model.
     * @param session HTTP-Session.
     * @return Testlauf-Detailtemplate oder Redirect auf Login.
     */
    @GetMapping("/ui/testruns/{id}")
    public String testRunDetails(@PathVariable Long id, Model model, HttpSession session) {
        User user = getCurrentUser(session);
        if (user == null) return "redirect:/login";

        TestRun run = testRunService.findById(id);
        model.addAttribute("testRun", run);

        List<TestCase> filteredTestCases = testCaseService.findAll().stream()
                .filter(tc -> tc.getRequirement().getId().equals(run.getRequirement().getId()))
                .collect(Collectors.toList());

        model.addAttribute("availableTestCases", filteredTestCases);
        model.addAttribute("allUsers", userService.findAll());
        model.addAttribute("newAssignment", new TestCaseTestRun());
        model.addAttribute("currentUser", user);

        return "testrun_details";
    }

    /**
     * Weist einen Testfall einem Testlauf und einem Tester zu.
     *
     * <p>
     * Status wird initial auf {@code ASSIGNED} gesetzt.
     * </p>
     *
     * <p>
     * Berechtigung: Testmanager (MANAGER) oder Admin.
     * </p>
     *
     * @param runId Testlauf-ID.
     * @param testCaseId Testfall-ID.
     * @param userId Tester-ID.
     * @param session HTTP-Session.
     * @param redirectAttributes Flash-Messages.
     * @return Redirect auf die Testlauf-Detailseite.
     */
    @PostMapping("/ui/testruns/{runId}/assign")
    public String assignTestCase(@PathVariable Long runId,
                                 @RequestParam Long testCaseId,
                                 @RequestParam Long userId,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        User user = getCurrentUser(session);
        if (user == null) return "redirect:/login";

        boolean allowed = "MANAGER".equals(user.getRole()) || "ADMIN".equals(user.getRole());
        if (!allowed) {
            redirectAttributes.addFlashAttribute("errorMessage", "Nur 'Testmanager' oder 'Admin' darf Tester zuweisen.");
            return "redirect:/ui/testruns/" + runId;
        }

        TestRun run = testRunService.findById(runId);
        TestCase tc = testCaseService.findById(testCaseId);
        User assignedUser = userService.findById(userId);

        TestCaseTestRun assignment = new TestCaseTestRun();
        assignment.setTestRun(run);
        assignment.setTestCase(tc);
        assignment.setTester(assignedUser);
        assignment.setStatus("ASSIGNED");
        testCaseTestRunService.save(assignment);

        updateTestRunStatus(run);
        return "redirect:/ui/testruns/" + runId;
    }

    /**
     * Entfernt eine Testfall-Zuordnung aus einem Testlauf.
     *
     * <p>
     * Fachliche Bedeutung: „aus Testlauf löschen“ bedeutet das Löschen der Zuordnung {@link TestCaseTestRun}.
     * Der Testfall selbst bleibt bestehen.
     * Nach dem Löschen werden Testlauf- und Requirement-Status neu berechnet.
     * </p>
     *
     * <p>
     * Berechtigung: Testmanager (MANAGER) oder Admin.
     * </p>
     *
     * @param runId Testlauf-ID.
     * @param assignmentId Zuordnungs-ID.
     * @param session HTTP-Session.
     * @param redirectAttributes Flash-Messages.
     * @return Redirect auf die Testlauf-Detailseite.
     */
    @PostMapping("/ui/testruns/{runId}/assignments/{assignmentId}/delete")
    public String deleteAssignmentFromTestRun(@PathVariable Long runId,
                                              @PathVariable Long assignmentId,
                                              HttpSession session,
                                              RedirectAttributes redirectAttributes) {
        User user = getCurrentUser(session);
        if (user == null) return "redirect:/login";

        boolean allowed = "MANAGER".equals(user.getRole()) || "ADMIN".equals(user.getRole());
        if (!allowed) {
            redirectAttributes.addFlashAttribute("errorMessage", "Nur 'Testmanager' oder 'Admin' darf Testfälle aus einem Testlauf entfernen.");
            return "redirect:/ui/testruns/" + runId;
        }

        TestCaseTestRun assignment = testCaseTestRunService.findById(assignmentId);
        TestRun run = assignment.getTestRun();

        testCaseTestRunService.deleteTestCaseTestRun(assignmentId);

        TestRun refreshedRun = testRunService.findById(run.getId());
        updateTestRunStatus(refreshedRun);

        redirectAttributes.addFlashAttribute("successMessage", "Testfall wurde aus dem Testlauf entfernt.");
        return "redirect:/ui/testruns/" + runId;
    }

    /**
     * Weist eine bestehende Zuordnung (TestCaseTestRun) einem anderen Tester zu.
     *
     * <p>
     * Fachliche Bedeutung: Reassign ändert die Zuordnung auf einen anderen Tester.
     * Der Status wird auf {@code ASSIGNED} gesetzt, damit die Aufgabe beim neuen Tester wieder als offen erscheint.
     * Anschließend wird der Testlaufstatus neu berechnet.
     * </p>
     *
     * <p>
     * Berechtigung: Testmanager (MANAGER) oder Admin.
     * </p>
     *
     * @param runId Testlauf-ID.
     * @param assignmentId Zuordnungs-ID.
     * @param userId ID des neuen Testers.
     * @param session HTTP-Session.
     * @param redirectAttributes Flash-Messages.
     * @return Redirect auf die Testlauf-Detailseite.
     */
    @PostMapping("/ui/testruns/{runId}/assignments/{assignmentId}/reassign")
    public String reassignTester(@PathVariable Long runId,
                                 @PathVariable Long assignmentId,
                                 @RequestParam Long userId,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        User user = getCurrentUser(session);
        if (user == null) return "redirect:/login";

        boolean allowed = "MANAGER".equals(user.getRole()) || "ADMIN".equals(user.getRole());
        if (!allowed) {
            redirectAttributes.addFlashAttribute("errorMessage", "Nur 'Testmanager' oder 'Admin' darf Tester neu zuweisen.");
            return "redirect:/ui/testruns/" + runId;
        }

        TestCaseTestRun assignment = testCaseTestRunService.findById(assignmentId);
        assignment.setTester(userService.findById(userId));
        assignment.setStatus("ASSIGNED");
        testCaseTestRunService.save(assignment);

        TestRun run = testRunService.findById(runId);
        updateTestRunStatus(run);

        redirectAttributes.addFlashAttribute("successMessage", "Tester wurde neu zugewiesen.");
        return "redirect:/ui/testruns/" + runId;
    }

    /**
     * Aktualisiert den Status einer Testausführung (Zuordnung) auf {@code PASSED} oder {@code FAILED}.
     *
     * <p>
     * Berechtigung:
     * </p>
     * <ul>
     *   <li>Tester darf den Status setzen, wenn ihm die Aufgabe zugewiesen ist.</li>
     *   <li>Manager und Admin dürfen ebenfalls setzen.</li>
     * </ul>
     *
     * @param assignmentId ID der Zuordnung.
     * @param status Neuer Status (erwartet: {@code PASSED} oder {@code FAILED}).
     * @param session HTTP-Session.
     * @param redirectAttributes Flash-Messages.
     * @return Redirect auf die Testlauf-Detailseite.
     */
    @PostMapping("/ui/execution/{assignmentId}")
    public String updateStatus(@PathVariable Long assignmentId,
                               @RequestParam String status,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        User user = getCurrentUser(session);
        if (user == null) return "redirect:/login";

        TestCaseTestRun execution = testCaseTestRunService.findById(assignmentId);

        boolean isAdmin = "ADMIN".equals(user.getRole());
        boolean isManager = "MANAGER".equals(user.getRole());
        boolean isTester = "TESTER".equals(user.getRole());

        boolean isAssignedTester = execution.getTester() != null
                && execution.getTester().getId() != null
                && execution.getTester().getId().equals(user.getId());

        boolean allowed = isAdmin || isManager || (isTester && isAssignedTester);
        if (!allowed) {
            redirectAttributes.addFlashAttribute("errorMessage", "Fehlende Berechtigung: Nur der zugewiesene Tester, Testmanager oder Admin darf den Status ändern.");
            return "redirect:/ui/testruns/" + execution.getTestRun().getId();
        }

        execution.setStatus(status);
        testCaseTestRunService.save(execution);

        TestRun run = execution.getTestRun();
        updateTestRunStatus(run);

        redirectAttributes.addFlashAttribute("successMessage", "Status wurde aktualisiert.");
        return "redirect:/ui/testruns/" + run.getId();
    }

    /**
     * Aktualisiert den Status eines Testlaufs gemäß den fachlichen Regeln.
     *
     * <p>
     * Regeln:
     * </p>
     * <ul>
     *   <li>{@code PLANNING}: solange keine Zuordnung existiert, die tatsächlich einem Tester zugewiesen ist.</li>
     *   <li>{@code FAILED}: sobald mindestens eine Ausführung {@code FAILED} ist.</li>
     *   <li>{@code SUCCESSFUL}: wenn mindestens eine Ausführung existiert und alle Ausführungen {@code PASSED} sind.</li>
     *   <li>{@code IN_PROGRESS}: in allen anderen Fällen (typischerweise wenn mind. eine Ausführung {@code ASSIGNED} ist).</li>
     * </ul>
     *
     * @param run Der Testlauf, dessen Status aktualisiert werden soll.
     */
    private void updateTestRunStatus(TestRun run) {
        boolean anyFailed = false;
        boolean allPassed = true;
        boolean hasAnyAssignedToTester = false;

        for (TestCaseTestRun tctr : run.getTestCaseTestRuns()) {
            if (tctr.getTester() != null) {
                hasAnyAssignedToTester = true;
            }
            if ("FAILED".equals(tctr.getStatus())) {
                anyFailed = true;
            }
            if (!"PASSED".equals(tctr.getStatus())) {
                allPassed = false;
            }
        }

        String newStatus;
        if (!hasAnyAssignedToTester) {
            newStatus = "PLANNING";
        } else if (anyFailed) {
            newStatus = "FAILED";
        } else if (allPassed && !run.getTestCaseTestRuns().isEmpty()) {
            newStatus = "SUCCESSFUL";
        } else {
            newStatus = "IN_PROGRESS";
        }

        if (!newStatus.equals(run.getStatus())) {
            run.setStatus(newStatus);
            testRunService.save(run);
            requirementService.refreshRequirementStatus(run.getRequirement().getId());
        }
    }
}