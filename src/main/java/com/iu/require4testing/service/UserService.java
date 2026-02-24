package com.iu.require4testing.service;

import com.iu.require4testing.dto.UserDTO;
import com.iu.require4testing.entity.User;
import com.iu.require4testing.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service-Klasse für die Verwaltung von Benutzern (Testern/Managern).
 * Dient als Schnittstelle zwischen Controller und Repository.
 * Stellt Methoden sowohl für die UI (Entity-basiert) als auch für die REST-API (DTO-basiert) bereit.
 *
 * @author Require4Testing Team
 * @version 1.0.0
 */
@Service
public class UserService {

    private final UserRepository repo;
    private final PasswordEncoder passwordEncoder;

    /**
     * Erstellt den Service und injiziert das {@link UserRepository} und {@link PasswordEncoder}.
     *
     * <p>Hinweis: Bei genau einem Konstruktor ist in Spring keine zusätzliche Annotation nötig.</p>
     *
     * @param repo Repository für Benutzer.
     * @param passwordEncoder Password encoder für Passwort-Hashing.
     */
    public UserService(UserRepository repo, PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
    }

    // --- UI Methoden (arbeiten direkt mit Entities) ---

    /**
     * Lädt alle Benutzer als Entities.
     *
     * @return Liste aller User
     */
    public List<User> findAll() {
        return repo.findAll();
    }

    /**
     * Findet einen Benutzer anhand der ID.
     *
     * @param id User ID
     * @return User Entity
     * @throws RuntimeException wenn nicht gefunden
     */
    public User findById(Long id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    // --- REST Controller Methoden (arbeiten mit DTOs) ---

    /**
     * Gibt alle Benutzer als DTOs zurück.
     *
     * @return Liste von UserDTOs
     */
    public List<UserDTO> getAllUsers() {
        return repo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    /**
     * Holt einen Benutzer als DTO anhand der ID.
     *
     * @param id User ID
     * @return UserDTO
     */
    public UserDTO getUserById(Long id) {
        return toDTO(findById(id));
    }

    /**
     * Sucht einen Benutzer anhand des Benutzernamens.
     *
     * @param username Der Benutzername
     * @return UserDTO oder null
     */
    public UserDTO getUserByUsername(String username) {
        return repo.findAll().stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .map(this::toDTO)
                .orElse(null);
    }

    /**
     * Erstellt einen neuen Benutzer aus einem DTO.
     *
     * @param dto Eingabe-DTO
     * @return Erstelltes UserDTO
     */
    public UserDTO createUser(UserDTO dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setRole(dto.getRole());
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        return toDTO(repo.save(user));
    }

    /**
     * Aktualisiert einen Benutzer.
     *
     * @param id ID des Benutzers
     * @param dto Update-Daten
     * @return Aktualisiertes UserDTO
     */
    public UserDTO updateUser(Long id, UserDTO dto) {
        User user = findById(id);
        user.setUsername(dto.getUsername());
        user.setRole(dto.getRole());
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        return toDTO(repo.save(user));
    }

    /**
     * Löscht einen Benutzer.
     *
     * @param id ID des zu löschenden Benutzers
     */
    public void deleteUser(Long id) {
        repo.deleteById(id);
    }

    /**
     * Hilfsmethode: Konvertiert Entity zu DTO.
     *
     * @param user User Entity
     * @return UserDTO
     */
    private UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setRole(user.getRole());
        return dto;
    }
}