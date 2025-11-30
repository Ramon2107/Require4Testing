package com.iu.require4testing.controller;

import com.iu.require4testing.dto.UserDTO;
import com.iu.require4testing.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST-Controller für Benutzer-Endpunkte.
 * Bietet CRUD-Operationen für Benutzer über die REST-API.
 * Implementiert Best Practices für RESTful Web Services.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    private final UserService userService;
    
    /**
     * Konstruktor mit Dependency Injection.
     * 
     * @param userService Der Benutzer-Service
     */
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    /**
     * Gibt alle Benutzer zurück.
     * 
     * @return Liste aller Benutzer
     */
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
    
    /**
     * Gibt einen Benutzer anhand seiner ID zurück.
     * 
     * @param id Die Benutzer-ID
     * @return Der Benutzer
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        UserDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }
    
    /**
     * Gibt einen Benutzer anhand seines Benutzernamens zurück.
     * 
     * @param username Der Benutzername
     * @return Der Benutzer
     */
    @GetMapping("/username/{username}")
    public ResponseEntity<UserDTO> getUserByUsername(@PathVariable String username) {
        UserDTO user = userService.getUserByUsername(username);
        return ResponseEntity.ok(user);
    }
    
    /**
     * Erstellt einen neuen Benutzer.
     * Die Eingabedaten werden automatisch validiert.
     * 
     * @param userDTO Die Benutzerdaten (validiert)
     * @return Der erstellte Benutzer
     */
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDTO) {
        UserDTO createdUser = userService.createUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }
    
    /**
     * Aktualisiert einen bestehenden Benutzer.
     * Die Eingabedaten werden automatisch validiert.
     * 
     * @param id Die Benutzer-ID
     * @param userDTO Die neuen Benutzerdaten (validiert)
     * @return Der aktualisierte Benutzer
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @Valid @RequestBody UserDTO userDTO) {
        UserDTO updatedUser = userService.updateUser(id, userDTO);
        return ResponseEntity.ok(updatedUser);
    }
    
    /**
     * Löscht einen Benutzer.
     * 
     * @param id Die Benutzer-ID
     * @return Leere Antwort
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
