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
 * Setzt das Rollenkonzept um und verwaltet die Workflows.
 *
 * <p>
 * Fachliche Regeln:
 * </p>
 * <ul>
 *   <li>Anforderungen bleiben in {@code PLANNING}, solange keine Testfälle existieren.</li>
 *   <li>Testläufe bleiben in {@code PLANNING}, solange keine Testfall-Zuordnung einem Tester zugewiesen ist.</li>
 *   <li>Testmanager (MANAGER) dürfen Testläufe verwalten und Ergebnisse erfassen, aber KEINE Testfälle in Anforderungen definieren oder löschen.</li>
 *   <li>Bestehende Ergebnisse (PASSED/FAILED) dürfen nur von ADMIN oder MANAGER nachträglich geändert werden.</li>
 *   <li>Testfälle dürfen innerhalb eines Testlaufs nicht mehrfach zugewiesen werden.</li>
 * </ul>
 *
 * <p>
 * Rollenbezeichnungen:
 * Admin, Tester, Testfallersteller, Testmanager, Requirements Engineer.
 * </p>
 *
 * @author Require4Testing Team
 * @version 4.7.0
 */
@Controller
public class UiController {

    private final RequirementService requirementService;
    private final TestCaseService testCaseService;
    private final TestRunService testRunService;
    private final TestCaseTestRunService testCaseTestRunService;
    private final UserService userService;

    /**
     * Erstellt den UI-Controller mit allen benötigten Services via Constructor Injection.
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
     * Liefert für Browser-Anfragen nach einem Favicon eine leere Antwort.
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
     * @param userId ID des ausgewählten Benutzers.
     * @param session HTTP-Session.
     * @return Redirect auf das Dashboard.
     */
    @PostMapping("/login")
    public String login(@RequestParam("userId") Long userId, HttpSession session) {
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
     * @param model Model.
     * @param session HTTP-Session.
     * @return Dashboard-Template oder Redirect auf Login.
     */
    @GetMapping("/")
    public String index(Model model, HttpSession session) {
        User user = getCurrentUser(session);
        if (user == null) return "redirect:/login";

        model.addAttribute("user", user);
        // Hinweis: Zur Unterdrückung von "Cannot resolve"-Meldungen im Template
        // wird das Model-Attribut "myTasks" immer gesetzt. Für Nicht-Tester ist es eine leere Liste.
        // Das verändert das Laufzeitverhalten nicht (die Tabelle ist ohnehin in einem th:if für TESTER).
        if ("TESTER".equals(user.getRole())) {
            model.addAttribute("myTasks", testCaseTestRunService.findAssignmentsByTester(user.getId()));
        } else {
            model.addAttribute("myTasks", java.util.Collections.emptyList());
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
     * <p>Berechtigung: Nur Requirements Engineer und Admin.</p>
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
    public String requirementDetails(@PathVariable("id") Long id, Model model, HttpSession session) {
        User user = getCurrentUser(session);
        if (user == null) return "redirect:/login";

        Requirement req = requirementService.findById(id);
        model.addAttribute("requirement", req);

        TestCase newTestCase = new TestCase();
        newTestCase.setRequirement(req);
        model.addAttribute("newTestCase", newTestCase);
        model.addAttribute("currentUser", user);
        model.addAttribute("userRole", user.getRole());

        return "requirement_details";
    }

    /**
     * Erstellt einen Testfall für eine Anforderung.
     * Berechtigung:
     * - Erlaubt: CREATOR (Testfallersteller), ADMIN
     * - Nicht erlaubt: REQUIREMENT_ENGINEER (darf Anforderungen definieren, aber keine Testfälle), MANAGER
     *
     * @param id Requirement-ID.
     * @param testCase Neuer Testfall aus dem Formular.
     * @param session HTTP-Session.
     * @param redirectAttributes Flash-Messages.
     * @return Redirect auf die Requirement-Detailseite.
     */
    @PostMapping("/ui/requirements/{id}/testcases")
    public String createTestCase(@PathVariable("id") Long id,
                                 @ModelAttribute TestCase testCase,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        User user = getCurrentUser(session);
        if (user == null) return "redirect:/login";

        // Requirements Engineers dürfen keine Testfälle anlegen
        boolean allowed = "CREATOR".equals(user.getRole()) || "ADMIN".equals(user.getRole());
        if (!allowed) {
            redirectAttributes.addFlashAttribute("errorMessage", "Fehlende Berechtigung: Nur Testfallersteller oder Admin dürfen Testfälle anlegen.");
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
     * Berechtigung:
     * - Erlaubt: CREATOR (Testfallersteller), ADMIN
     * - Nicht erlaubt: REQUIREMENT_ENGINEER (darf Anforderungen definieren, aber keine Testfälle), MANAGER
     *
     * @param reqId Requirement-ID.
     * @param tcId TestCase-ID.
     * @param session HTTP-Session.
     * @param redirectAttributes Flash-Messages.
     * @return Redirect zurück auf die Requirement-Detailseite.
     */
    @PostMapping("/ui/requirements/{reqId}/testcases/{tcId}/delete")
    public String deleteTestCaseFromRequirement(@PathVariable("reqId") Long reqId,
                                                @PathVariable("tcId") Long tcId,
                                                HttpSession session,
                                                RedirectAttributes redirectAttributes) {
        User user = getCurrentUser(session);
        if (user == null) return "redirect:/login";

        // Requirements Engineers dürfen keine Testfälle löschen
        boolean allowed = "CREATOR".equals(user.getRole()) || "ADMIN".equals(user.getRole());
        if (!allowed) {
            redirectAttributes.addFlashAttribute("errorMessage", "Fehlende Berechtigung: Nur Testfallersteller oder Admin dürfen Testfälle löschen.");
            return "redirect:/ui/requirements/" + reqId;
        }

        testCaseService.deleteTestCaseFromRequirement(tcId);
        requirementService.refreshRequirementStatus(reqId);

        redirectAttributes.addFlashAttribute("successMessage", "Testfall wurde entfernt.");
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
     * <p>Berechtigung: Nur Testmanager (MANAGER) und Admin.</p>
     *
     * @param testRun Testlauf aus dem Formular.
     * @param requirementId Zugehörige Requirement-ID.
     * @param session HTTP-Session.
     * @param redirectAttributes Flash-Messages.
     * @return Redirect auf die Testläufe-Liste.
     */
    @PostMapping("/ui/testruns")
    public String createTestRun(@ModelAttribute TestRun testRun,
                                @RequestParam("requirementId") Long requirementId,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        User user = getCurrentUser(session);
        if (user == null) return "redirect:/login";

        boolean allowed = "MANAGER".equals(user.getRole()) || "ADMIN".equals(user.getRole());
        if (!allowed) {
            redirectAttributes.addFlashAttribute("errorMessage", "Nur 'Testmanager' oder 'Admin' darf Testläufe erstellen.");
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
    public String testRunDetails(@PathVariable("id") Long id, Model model, HttpSession session) {
        User user = getCurrentUser(session);
        if (user == null) return "redirect:/login";

        TestRun run = testRunService.findById(id);
        model.addAttribute("testRun", run);

        List<TestCase> filteredTestCases = testCaseService.findAll().stream()
                .filter(tc -> tc.getRequirement().getId().equals(run.getRequirement().getId()))
                .collect(Collectors.toList());

        model.addAttribute("availableTestCases", filteredTestCases);
        model.addAttribute("allUsers", userService.findAll());
        model.addAttribute("currentUser", user);

        return "testrun_details";
    }

    /**
     * Weist einen Testfall einem Testlauf und einem Tester zu.
     *
     * <p>Berechtigung: Nur Testmanager (MANAGER) und Admin.</p>
     *
     * @param runId Testlauf-ID.
     * @param testCaseId Testfall-ID.
     * @param userId Tester-ID.
     * @param session HTTP-Session.
     * @param redirectAttributes Flash-Messages.
     * @return Redirect auf die Testlauf-Detailseite.
     */
    @PostMapping("/ui/testruns/{runId}/assign")
    public String assignTestCase(@PathVariable("runId") Long runId,
                                 @RequestParam("testCaseId") Long testCaseId,
                                 @RequestParam("userId") Long userId,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        User user = getCurrentUser(session);
        if (user == null || (!"MANAGER".equals(user.getRole()) && !"ADMIN".equals(user.getRole()))) return "redirect:/ui/testruns/" + runId;

        // Fachliche Validierung: Dublettenprüfung
        if (testCaseTestRunService.existsAssignment(runId, testCaseId)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Dieser Testfall ist diesem Testlauf bereits zugeordnet. Pro Lauf kann jeder Testfall nur einmal zugewiesen werden.");
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
     * <p>Berechtigung: Nur Testmanager (MANAGER) und Admin.</p>
     *
     * @param runId Testlauf-ID.
     * @param assignmentId Zuordnungs-ID.
     * @param session HTTP-Session.
     * @return Redirect auf die Testlauf-Detailseite.
     */
    @PostMapping("/ui/testruns/{runId}/assignments/{assignmentId}/delete")
    public String deleteAssignmentFromTestRun(@PathVariable("runId") Long runId,
                                              @PathVariable("assignmentId") Long assignmentId,
                                              HttpSession session) {
        User user = getCurrentUser(session);
        if (user == null || (!"MANAGER".equals(user.getRole()) && !"ADMIN".equals(user.getRole()))) return "redirect:/ui/testruns/" + runId;

        testCaseTestRunService.deleteTestCaseTestRun(assignmentId);
        updateTestRunStatus(testRunService.findById(runId));

        return "redirect:/ui/testruns/" + runId;
    }

    /**
     * Weist eine bestehende Zuordnung einem anderen Tester zu.
     *
     * <p>Berechtigung: Nur Testmanager (MANAGER) und Admin.</p>
     *
     * @param runId Testlauf-ID.
     * @param assignmentId Zuordnungs-ID.
     * @param userId ID des neuen Testers.
     * @param session HTTP-Session.
     * @return Redirect auf die Testlauf-Detailseite.
     */
    @PostMapping("/ui/testruns/{runId}/assignments/{assignmentId}/reassign")
    public String reassignTester(@PathVariable("runId") Long runId,
                                 @PathVariable("assignmentId") Long assignmentId,
                                 @RequestParam("userId") Long userId,
                                 HttpSession session) {
        User user = getCurrentUser(session);
        if (user == null || (!"MANAGER".equals(user.getRole()) && !"ADMIN".equals(user.getRole()))) return "redirect:/ui/testruns/" + runId;

        TestCaseTestRun assignment = testCaseTestRunService.findById(assignmentId);
        assignment.setTester(userService.findById(userId));
        assignment.setStatus("ASSIGNED");
        testCaseTestRunService.save(assignment);

        updateTestRunStatus(testRunService.findById(runId));
        return "redirect:/ui/testruns/" + runId;
    }

    /**
     * Erfasst das Testergebnis für eine Zuweisung.
     *
     * <p>Berechtigung: Zugewiesener Tester, Manager oder Admin.</p>
     * <p>Admin-Lock: Bestehende Ergebnisse (PASSED/FAILED) dürfen nur von ADMIN oder MANAGER nachträglich geändert werden.</p>
     *
     * @param assignmentId ID der Zuweisung.
     * @param status Neuer Status (PASSED/FAILED).
     * @param notes Optionale Notizen.
     * @param session HTTP-Session.
     * @param redirectAttributes Flash-Messages.
     * @return Redirect zur Detailansicht des Testlaufs.
     */
    @PostMapping("/ui/execution/{assignmentId}")
    public String updateStatus(@PathVariable("assignmentId") Long assignmentId,
                               @RequestParam("status") String status,
                               @RequestParam(value = "notes", required = false) String notes,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        User user = getCurrentUser(session);
        if (user == null) return "redirect:/login";

        TestCaseTestRun execution = testCaseTestRunService.findById(assignmentId);
        boolean isAdmin = "ADMIN".equals(user.getRole());
        boolean isManager = "MANAGER".equals(user.getRole());

        // Unveränderbarkeit / Admin-Lock
        boolean isFinished = "PASSED".equals(execution.getStatus()) || "FAILED".equals(execution.getStatus());
        if (isFinished && !isAdmin && !isManager) {
            redirectAttributes.addFlashAttribute("errorMessage", "Abgeschlossene Tests sind gesperrt. Eine Änderung des Ergebnisses ist nur für Admins oder Testmanager möglich.");
            return "redirect:/ui/testruns/" + execution.getTestRun().getId();
        }

        boolean isAssigned = execution.getTester() != null && execution.getTester().getId().equals(user.getId());
        if (isAdmin || isManager || ("TESTER".equals(user.getRole()) && isAssigned)) {
            execution.setStatus(status);
            execution.setNotes(notes);
            testCaseTestRunService.save(execution);
            updateTestRunStatus(execution.getTestRun());
            redirectAttributes.addFlashAttribute("successMessage", "Ergebnis erfasst.");
            return "redirect:/ui/testruns/" + execution.getTestRun().getId();
        }

        redirectAttributes.addFlashAttribute("errorMessage", "Keine Berechtigung zur Ergebniserfassung.");
        return "redirect:/ui/testruns/" + execution.getTestRun().getId();
    }

    /**
     * Berechnet den Status eines Testlaufs basierend auf seinen Zuweisungen neu.
     * Kaskadiert die Statusänderung auch zum zugehörigen Requirement.
     *
     * <p>
     * Regeln:
     * - Einer FAILED -> Testlauf FAILED.
     * - Alle PASSED -> Testlauf SUCCESSFUL.
     * - Sonst IN_PROGRESS oder PLANNING.
     * </p>
     *
     * @param run Der zu aktualisierende Testlauf.
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
            // Kaskadierung zum Requirement
            requirementService.refreshRequirementStatus(run.getRequirement().getId());
        }
    }
}