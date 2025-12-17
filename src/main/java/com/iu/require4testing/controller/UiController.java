package com.iu.require4testing.controller;

import com.iu.require4testing.entity.*;
import com.iu.require4testing.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Haupt-Controller für die Web-Oberfläche (UI).
 * Setzt das Rollenkonzept strikt um und verwaltet die Workflows.
 *
 * @author Require4Testing Team
 * @version 3.5.0
 */
@Controller
public class UiController {

    @Autowired private RequirementService requirementService;
    @Autowired private TestCaseService testCaseService;
    @Autowired private TestRunService testRunService;
    @Autowired private TestCaseTestRunService testCaseTestRunService;
    @Autowired private UserService userService;

    // --- SESSION & AUTH ---

    /** Zeigt Login */
    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("users", userService.findAll());
        return "login";
    }

    /** Login Logik */
    @PostMapping("/login")
    public String login(@RequestParam Long userId, HttpSession session) {
        session.setAttribute("currentUser", userService.findById(userId));
        return "redirect:/";
    }

    /** Logout Logik */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    private User getCurrentUser(HttpSession session) {
        return (User) session.getAttribute("currentUser");
    }

    /** Dashboard */
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

    @GetMapping("/ui/requirements")
    public String listRequirements(Model model, HttpSession session) {
        if (getCurrentUser(session) == null) return "redirect:/login";
        model.addAttribute("requirements", requirementService.findAll());
        model.addAttribute("newRequirement", new Requirement());
        return "requirements";
    }

    /**
     * Erstellt Anforderung.
     * Berechtigung: NUR Requirements Engineer und Admin.
     */
    @PostMapping("/ui/requirements")
    public String createRequirement(@ModelAttribute Requirement requirement, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = getCurrentUser(session);
        if (user == null) return "redirect:/login";

        boolean allowed = "REQUIREMENT_ENGINEER".equals(user.getRole()) || "ADMIN".equals(user.getRole());

        if (!allowed) {
            redirectAttributes.addFlashAttribute("errorMessage", "Fehlende Berechtigung: Nur 'Requirements Engineer' darf Anforderungen erstellen.");
            return "redirect:/ui/requirements";
        }

        requirement.setCreatedBy(user.getId());
        requirementService.save(requirement);
        redirectAttributes.addFlashAttribute("successMessage", "Anforderung erstellt.");
        return "redirect:/ui/requirements";
    }

    // --- TESTFÄLLE ---

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
     * Erstellt Testfall.
     * Berechtigung: NUR Testfallersteller (CREATOR) und Admin.
     */
    @PostMapping("/ui/requirements/{id}/testcases")
    public String createTestCase(@PathVariable Long id, @ModelAttribute TestCase testCase, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = getCurrentUser(session);
        if (user == null) return "redirect:/login";

        boolean allowed = "CREATOR".equals(user.getRole()) || "ADMIN".equals(user.getRole());

        if (!allowed) {
            redirectAttributes.addFlashAttribute("errorMessage", "Fehlende Berechtigung: Nur 'Testfallersteller' darf Testfälle erstellen.");
            return "redirect:/ui/requirements/" + id;
        }

        Requirement req = requirementService.findById(id);
        testCase.setRequirement(req);
        testCase.setCreatedBy(user.getId());
        testCase.setId(null);
        testCaseService.save(testCase);

        redirectAttributes.addFlashAttribute("successMessage", "Testfall erstellt.");
        return "redirect:/ui/requirements/" + id;
    }

    // --- TESTLÄUFE ---

    @GetMapping("/ui/testruns")
    public String listTestRuns(Model model, HttpSession session) {
        if (getCurrentUser(session) == null) return "redirect:/login";
        model.addAttribute("testRuns", testRunService.findAll());
        model.addAttribute("requirements", requirementService.findAll());
        model.addAttribute("newTestRun", new TestRun());
        return "testruns";
    }

    /**
     * Erstellt Testlauf.
     * Berechtigung: NUR Testmanager (MANAGER) und Admin.
     */
    @PostMapping("/ui/testruns")
    public String createTestRun(@ModelAttribute TestRun testRun, @RequestParam Long requirementId, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = getCurrentUser(session);
        if (user == null) return "redirect:/login";

        boolean allowed = "MANAGER".equals(user.getRole()) || "ADMIN".equals(user.getRole());

        if (!allowed) {
            redirectAttributes.addFlashAttribute("errorMessage", "Fehlende Berechtigung: Nur 'Testmanager' darf Testläufe erstellen.");
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

    // --- TESTDURCHFÜHRUNG ---

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
     * Weist Testfall zu.
     * Auch hier: Eher Testmanager Aufgabe, Admin erlaubt.
     */
    @PostMapping("/ui/testruns/{runId}/assign")
    public String assignTestCase(@PathVariable Long runId, @RequestParam Long testCaseId, @RequestParam Long userId, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = getCurrentUser(session);
        if (user == null) return "redirect:/login";

        boolean allowed = "MANAGER".equals(user.getRole()) || "ADMIN".equals(user.getRole());
        if (!allowed) {
            redirectAttributes.addFlashAttribute("errorMessage", "Nur 'Testmanager' darf Tester zuweisen.");
            return "redirect:/ui/testruns/" + runId;
        }

        TestRun run = testRunService.findById(runId);
        TestCase tc = testCaseService.findById(testCaseId);
        User assignedUser = userService.findById(userId);

        TestCaseTestRun assignment = new TestCaseTestRun();
        assignment.setTestRun(run);
        assignment.setTestCase(tc);
        assignment.setTester(assignedUser);
        assignment.setStatus("OPEN");
        testCaseTestRunService.save(assignment);

        updateTestRunStatus(run);
        return "redirect:/ui/testruns/" + runId;
    }

    /**
     * Status Update.
     * Berechtigung: Tester (dem es zugeordnet ist), Manager, Admin.
     */
    @PostMapping("/ui/execution/{assignmentId}")
    public String updateStatus(@PathVariable Long assignmentId, @RequestParam String status, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = getCurrentUser(session);
        if (user == null) return "redirect:/login";

        // Tester (generell), Manager, Admin erlaubt
        boolean allowed = "TESTER".equals(user.getRole()) || "MANAGER".equals(user.getRole()) || "ADMIN".equals(user.getRole());
        if (!allowed) {
            redirectAttributes.addFlashAttribute("errorMessage", "Fehlende Berechtigung zur Testausführung.");
        }

        TestCaseTestRun execution = testCaseTestRunService.findById(assignmentId);


        execution.setStatus(status);
        testCaseTestRunService.save(execution);

        TestRun run = execution.getTestRun();
        updateTestRunStatus(run);

        return "redirect:/ui/testruns/" + run.getId();
    }

    private void updateTestRunStatus(TestRun run) {
        boolean anyFailed = false;
        boolean anyOpen = false;
        boolean allPassed = true;

        for (TestCaseTestRun tctr : run.getTestCaseTestRuns()) {
            if ("FAILED".equals(tctr.getStatus())) anyFailed = true;
            if ("OPEN".equals(tctr.getStatus())) { anyOpen = true; allPassed = false; }
            if (!"PASSED".equals(tctr.getStatus())) allPassed = false;
        }

        String newStatus = "IN_PROGRESS";
        if (anyFailed) newStatus = "FAILED";
        else if (allPassed && !run.getTestCaseTestRuns().isEmpty()) newStatus = "SUCCESSFUL";
        else if (anyOpen) newStatus = "IN_PROGRESS";
        else if (run.getTestCaseTestRuns().isEmpty()) newStatus = "PLANNING";

        if (!newStatus.equals(run.getStatus())) {
            run.setStatus(newStatus);
            testRunService.save(run);
            requirementService.refreshRequirementStatus(run.getRequirement().getId());
        }
    }
}