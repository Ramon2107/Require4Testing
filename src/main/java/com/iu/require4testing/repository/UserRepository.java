package com.iu.require4testing.repository;

import com.iu.require4testing.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository für Benutzer-Entitäten.
 * Bietet CRUD-Operationen und benutzerdefinierte Abfragen für Benutzer.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Findet einen Benutzer anhand seiner E-Mail-Adresse.
     * 
     * @param email Die E-Mail-Adresse des Benutzers
     * @return Optional mit dem Benutzer, falls gefunden
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Findet einen Benutzer anhand seines Benutzernamens.
     * 
     * @param username Der Benutzername
     * @return Optional mit dem Benutzer, falls gefunden
     */
    Optional<User> findByUsername(String username);
    
    /**
     * Prüft, ob ein Benutzer mit der angegebenen E-Mail existiert.
     * 
     * @param email Die E-Mail-Adresse
     * @return true, wenn ein Benutzer existiert
     */
    boolean existsByEmail(String email);
    
    /**
     * Prüft, ob ein Benutzer mit dem angegebenen Benutzernamen existiert.
     * 
     * @param username Der Benutzername
     * @return true, wenn ein Benutzer existiert
     */
    boolean existsByUsername(String username);
}
