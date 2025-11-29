package com.iu.require4testing.entity;

import jakarta.persistence.*;

/**
 * Entity für die Many-to-Many-Zuordnung zwischen Testfällen und Testläufen.
 */
@Entity
@Table(name = "test_case_test_run", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"testCaseId", "testRunId"})
})
public class TestCaseTestRun {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long testCaseId;

    @Column(nullable = false)
    private Long testRunId;

    @Column(nullable = false)
    private Long testerId;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getTestCaseId() { return testCaseId; }
    public void setTestCaseId(Long testCaseId) { this.testCaseId = testCaseId; }
    public Long getTestRunId() { return testRunId; }
    public void setTestRunId(Long testRunId) { this.testRunId = testRunId; }
    public Long getTesterId() { return testerId; }
    public void setTesterId(Long testerId) { this.testerId = testerId; }
}