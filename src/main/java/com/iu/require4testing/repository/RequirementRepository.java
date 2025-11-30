package com.iu.require4testing.repository;

import com.iu.require4testing.entity.Requirement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository für Anforderungs-Entitäten.
 * Bietet CRUD-Operationen und benutzerdefinierte Abfragen für Anforderungen.
 */
@Repository
public interface RequirementRepository extends JpaRepository<Requirement, Long> {
    
    /**
     * Findet alle Anforderungen eines bestimmten Erstellers.
     * 
     * @param createdBy Die ID des Erstellers
     * @return Liste der Anforderungen
     */
    List<Requirement> findByCreatedBy(Long createdBy);
    
    /**
     * Findet Anforderungen, deren Name einen bestimmten Suchbegriff enthält.
     * 
     * @param name Der Suchbegriff
     * @return Liste der gefundenen Anforderungen
     */
    List<Requirement> findByNameContainingIgnoreCase(String name);
}
