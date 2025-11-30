package com.iu.require4testing.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Globaler Exception-Handler für die REST-API.
 * Behandelt alle Exceptions zentral und gibt einheitliche Fehlermeldungen zurück.
 * Implementiert Best Practices für API-Fehlerbehandlung.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    /**
     * Behandelt Validierungsfehler aus @Valid-Annotationen.
     * Gibt detaillierte Informationen zu allen Validierungsfehlern zurück.
     * 
     * @param ex Die MethodArgumentNotValidException
     * @param request Die Web-Anfrage
     * @return ResponseEntity mit Validierungsfehlerdetails
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(
            MethodArgumentNotValidException ex, WebRequest request) {
        
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Validierungsfehler");
        
        // Sammle alle Feldvalidierungsfehler
        List<Map<String, String>> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::mapFieldError)
                .collect(Collectors.toList());
        
        body.put("message", "Die Eingabedaten sind ungültig");
        body.put("fieldErrors", fieldErrors);
        body.put("path", request.getDescription(false));
        
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
    
    /**
     * Wandelt einen FieldError in eine Map um.
     * Sensible Felder wie Passwörter werden aus Sicherheitsgründen nicht angezeigt.
     * 
     * @param error Der FieldError
     * @return Map mit Feldname und Fehlermeldung
     */
    private Map<String, String> mapFieldError(FieldError error) {
        Map<String, String> errorMap = new LinkedHashMap<>();
        errorMap.put("field", error.getField());
        errorMap.put("message", error.getDefaultMessage());
        
        // Sensible Felder nicht in der Fehlerantwort anzeigen
        String fieldName = error.getField().toLowerCase();
        if (fieldName.contains("password") || fieldName.contains("passwort") 
                || fieldName.contains("secret") || fieldName.contains("token")) {
            errorMap.put("rejectedValue", "[VERSTECKT]");
        } else {
            errorMap.put("rejectedValue", error.getRejectedValue() != null 
                    ? error.getRejectedValue().toString() : "null");
        }
        return errorMap;
    }
    
    /**
     * Behandelt ResourceNotFoundException.
     * 
     * @param ex Die Exception
     * @param request Die Web-Anfrage
     * @return ResponseEntity mit Fehlerdetails
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(
            ResourceNotFoundException ex, WebRequest request) {
        
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.NOT_FOUND.value());
        body.put("error", "Nicht gefunden");
        body.put("message", ex.getMessage());
        body.put("path", request.getDescription(false));
        
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }
    
    /**
     * Behandelt IllegalArgumentException.
     * 
     * @param ex Die Exception
     * @param request Die Web-Anfrage
     * @return ResponseEntity mit Fehlerdetails
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(
            IllegalArgumentException ex, WebRequest request) {
        
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Ungültige Anfrage");
        body.put("message", ex.getMessage());
        body.put("path", request.getDescription(false));
        
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
    
    /**
     * Behandelt alle anderen Exceptions.
     * Gibt aus Sicherheitsgründen keine internen Details preis.
     * 
     * @param ex Die Exception
     * @param request Die Web-Anfrage
     * @return ResponseEntity mit Fehlerdetails
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllExceptions(
            Exception ex, WebRequest request) {
        
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("error", "Interner Serverfehler");
        // Aus Sicherheitsgründen keine internen Details in der API-Antwort
        body.put("message", "Ein unerwarteter Fehler ist aufgetreten. Bitte kontaktieren Sie den Support.");
        body.put("path", request.getDescription(false));
        
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
