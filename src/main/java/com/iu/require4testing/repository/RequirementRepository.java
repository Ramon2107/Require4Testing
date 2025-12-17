package com.iu.require4testing.repository;

import com.iu.require4testing.entity.Requirement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository für Requirements.
 */
@Repository
public interface RequirementRepository extends JpaRepository<Requirement, Long> {

    /**
     * Findet die letzte Anforderung mit einem bestimmten ID-Präfix (z.B. "REQ-2025-").
     * Wird benötigt, um die nächste laufende Nummer zu ermitteln.
     */
    Requirement findTopByReadableIdStartingWithOrderByReadableIdDesc(String prefix);
}