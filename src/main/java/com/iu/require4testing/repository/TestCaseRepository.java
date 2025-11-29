package com.iu.require4testing.repository;

import com.iu.require4testing.entity.TestCase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository für Testfall-Entitäten.
 * Bietet CRUD-Operationen und benutzerdefinierte Abfragen für Testfälle.
 */
@Repository
public interface TestCaseRepository extends JpaRepository<TestCase, Long> {
    
    /**
     * Findet alle Testfälle einer bestimmten Anforderung.
     * 
     * @param requirementId Die ID der Anforderung
     * @return Liste der Testfälle
     */
    List<TestCase> findByRequirementId(Long requirementId);
    
    /**
     * Findet alle Testfälle eines bestimmten Erstellers.
     * 
     * @param createdBy Die ID des Erstellers
     * @return Liste der Testfälle
     */
    List<TestCase> findByCreatedBy(Long createdBy);
    
    /**
     * Findet Testfälle, deren Name einen bestimmten Suchbegriff enthält.
     * 
     * @param name Der Suchbegriff
     * @return Liste der gefundenen Testfälle
     */
    List<TestCase> findByNameContainingIgnoreCase(String name);
}
