package com.iu.require4testing.repository;

import com.iu.require4testing.entity.TestRun;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository für Testlauf-Entitäten.
 * Bietet CRUD-Operationen und benutzerdefinierte Abfragen für Testläufe.
 */
@Repository
public interface TestRunRepository extends JpaRepository<TestRun, Long> {
    
    /**
     * Findet alle Testläufe eines bestimmten Erstellers.
     * 
     * @param createdBy Die ID des Erstellers
     * @return Liste der Testläufe
     */
    List<TestRun> findByCreatedBy(Long createdBy);
    
    /**
     * Findet Testläufe nach Status.
     * 
     * @param status Der Status
     * @return Liste der Testläufe
     */
    List<TestRun> findByStatus(String status);
    
    /**
     * Findet Testläufe, deren Name einen bestimmten Suchbegriff enthält.
     * 
     * @param name Der Suchbegriff
     * @return Liste der gefundenen Testläufe
     */
    List<TestRun> findByNameContainingIgnoreCase(String name);
}
