package com.iu.require4testing.dto;

/**
 * Data Transfer Object für Testfall-Testlauf-Zuordnungen.
 * Wird für die API-Kommunikation verwendet, um Testfälle einem Testlauf
 * und einem Tester zuzuordnen.
 */
public class TestCaseTestRunDTO {
    
    private Long id;
    private Long testCaseId;
    private Long testRunId;
    private Long testerId;
    
    /**
     * Standardkonstruktor.
     */
    public TestCaseTestRunDTO() {}
    
    /**
     * Konstruktor mit allen Feldern.
     * 
     * @param id Zuordnungs-ID
     * @param testCaseId ID des Testfalls
     * @param testRunId ID des Testlaufs
     * @param testerId ID des zugewiesenen Testers
     */
    public TestCaseTestRunDTO(Long id, Long testCaseId, Long testRunId, Long testerId) {
        this.id = id;
        this.testCaseId = testCaseId;
        this.testRunId = testRunId;
        this.testerId = testerId;
    }
    
    /**
     * Gibt die ID der Zuordnung zurück.
     * 
     * @return Die Zuordnungs-ID
     */
    public Long getId() {
        return id;
    }
    
    /**
     * Setzt die ID der Zuordnung.
     * 
     * @param id Die Zuordnungs-ID
     */
    public void setId(Long id) {
        this.id = id;
    }
    
    /**
     * Gibt die ID des Testfalls zurück.
     * 
     * @return Die Testfall-ID
     */
    public Long getTestCaseId() {
        return testCaseId;
    }
    
    /**
     * Setzt die ID des Testfalls.
     * 
     * @param testCaseId Die Testfall-ID
     */
    public void setTestCaseId(Long testCaseId) {
        this.testCaseId = testCaseId;
    }
    
    /**
     * Gibt die ID des Testlaufs zurück.
     * 
     * @return Die Testlauf-ID
     */
    public Long getTestRunId() {
        return testRunId;
    }
    
    /**
     * Setzt die ID des Testlaufs.
     * 
     * @param testRunId Die Testlauf-ID
     */
    public void setTestRunId(Long testRunId) {
        this.testRunId = testRunId;
    }
    
    /**
     * Gibt die ID des zugewiesenen Testers zurück.
     * 
     * @return Die Tester-ID
     */
    public Long getTesterId() {
        return testerId;
    }
    
    /**
     * Setzt die ID des zugewiesenen Testers.
     * 
     * @param testerId Die Tester-ID
     */
    public void setTesterId(Long testerId) {
        this.testerId = testerId;
    }
}
