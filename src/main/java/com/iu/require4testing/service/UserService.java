package com.iu.require4testing.service;

import com.iu.require4testing.dto.UserDTO;
import com.iu.require4testing.entity.User;
import com.iu.require4testing.exception.ResourceNotFoundException;
import com.iu.require4testing.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service für Benutzer-Operationen.
 * Bietet Geschäftslogik für die Verwaltung von Benutzern.
 */
@Service
@Transactional
public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    /**
     * Gibt alle Benutzer zurück.
     * 
     * @return Liste aller Benutzer als DTOs
     */
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Findet einen Benutzer anhand seiner ID.
     * 
     * @param id Die Benutzer-ID
     * @return Das Benutzer-DTO
     * @throws ResourceNotFoundException wenn der Benutzer nicht gefunden wird
     */
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Benutzer", "ID", id));
        return convertToDTO(user);
    }
    
    /**
     * Findet einen Benutzer anhand seines Benutzernamens.
     * 
     * @param username Der Benutzername
     * @return Das Benutzer-DTO
     * @throws ResourceNotFoundException wenn der Benutzer nicht gefunden wird
     */
    public UserDTO getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Benutzer", "Benutzername", username));
        return convertToDTO(user);
    }
    
    /**
     * Erstellt einen neuen Benutzer.
     * Das Passwort wird mit BCrypt gehasht, bevor es gespeichert wird.
     * 
     * @param userDTO Die Benutzerdaten
     * @return Das erstellte Benutzer-DTO
     */
    public UserDTO createUser(UserDTO userDTO) {
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new IllegalArgumentException("Benutzername bereits vergeben");
        }
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new IllegalArgumentException("E-Mail bereits vergeben");
        }
        
        User user = convertToEntity(userDTO);
        User savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }
    
    /**
     * Aktualisiert einen bestehenden Benutzer.
     * Falls ein neues Passwort angegeben wird, wird es mit BCrypt gehasht.
     * 
     * @param id Die Benutzer-ID
     * @param userDTO Die neuen Benutzerdaten
     * @return Das aktualisierte Benutzer-DTO
     * @throws ResourceNotFoundException wenn der Benutzer nicht gefunden wird
     */
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Benutzer", "ID", id));
        
        existingUser.setUsername(userDTO.getUsername());
        existingUser.setEmail(userDTO.getEmail());
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            // Passwort wird mit BCrypt gehasht, bevor es gespeichert wird
            existingUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
        if (userDTO.getRole() != null) {
            existingUser.setRole(userDTO.getRole());
        }
        
        User updatedUser = userRepository.save(existingUser);
        return convertToDTO(updatedUser);
    }
    
    /**
     * Löscht einen Benutzer.
     * 
     * @param id Die Benutzer-ID
     * @throws ResourceNotFoundException wenn der Benutzer nicht gefunden wird
     */
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("Benutzer", "ID", id);
        }
        userRepository.deleteById(id);
    }
    
    /**
     * Konvertiert eine User-Entity in ein UserDTO.
     * Das Passwort wird aus Sicherheitsgründen nicht in das DTO übertragen.
     * 
     * @param user Die User-Entity
     * @return Das UserDTO (ohne Passwort)
     */
    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        // Passwort wird aus Sicherheitsgründen nicht zurückgegeben
        dto.setRole(user.getRole());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        return dto;
    }
    
    /**
     * Konvertiert ein UserDTO in eine User-Entity.
     * Das Passwort wird mit BCrypt gehasht.
     * 
     * @param dto Das UserDTO
     * @return Die User-Entity mit gehashtem Passwort
     */
    private User convertToEntity(UserDTO dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        // Passwort wird mit BCrypt gehasht, bevor es gespeichert wird
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setEmail(dto.getEmail());
        user.setRole(dto.getRole() != null ? dto.getRole() : "USER");
        return user;
    }
}
