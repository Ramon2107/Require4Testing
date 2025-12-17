package com.iu.require4testing.repository;

import com.iu.require4testing.entity.TestRun;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository für TestRun-Entitäten.
 */
@Repository
public interface TestRunRepository extends JpaRepository<TestRun, Long> {

    /**
     * Findet alle Testläufe für eine bestimmte Anforderung.
     * Wichtig für die Statusberechnung der Anforderung.
     */
    List<TestRun> findByRequirementId(Long requirementId);
}