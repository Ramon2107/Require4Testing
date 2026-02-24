package com.iu.require4testing.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Globale Ausnahmebehandlung für die Anwendung.
 *
 * <p>
 * Diese Klasse stellt sicher, dass Fehlerfälle konsistent behandelt werden.
 * Besonders wichtig: Fehlende statische Ressourcen (z.B. CSS/JS/WebJars/Favicon)
 * dürfen nicht als JSON mit Status 500 beantwortet werden, da Browser diese Antworten
 * wegen des falschen MIME-Typs blockieren (z.B. "application/json" statt "text/css").
 * </p>
 *
 * <p>
 * Daher wird {@link NoResourceFoundException} explizit als HTTP 404 behandelt.
 * </p>
 *
 * @author Require4Testing Team
 * @version 1.1.0
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Behandelt fehlende Ressourcen (statische Inhalte) korrekt als HTTP 404.
     *
     * <p>
     * Typische Fälle:
     * </p>
     * <ul>
     *   <li>/webjars/** (Bootstrap, Bootstrap Icons)</li>
     *   <li>/favicon.ico</li>
     *   <li>/css/**, /js/**, /images/** (falls vorhanden)</li>
     * </ul>
     *
     * <p>
     * Das verhindert, dass Browser Stylesheets oder Skripte blockieren, weil sie statt CSS/JS eine JSON-Fehlerseite erhalten.
     * </p>
     *
     * @param ex Ausnahme, die ausgelöst wurde, weil eine Ressource nicht gefunden wurde.
     * @return HTTP 404 mit Fehlerdetails im JSON-Format.
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNoResourceFound(NoResourceFoundException ex) {
        Map<String, Object> body = buildErrorBody(HttpStatus.NOT_FOUND, ex.getMessage());
        body.put("path", ex.getResourcePath());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    /**
     * Behandelt "Resource not found" aus der Business-Logik.
     *
     * <p>
     * Hinweis: Das ist nicht die gleiche Kategorie wie {@link NoResourceFoundException} (statische Ressourcen).
     * Diese Exception steht typischerweise für "Datensatz nicht gefunden".
     * </p>
     *
     * @param ex Ausnahme.
     * @return HTTP 404 mit Fehlerdetails.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFound(ResourceNotFoundException ex) {
        Map<String, Object> body = buildErrorBody(HttpStatus.NOT_FOUND, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    /**
     * Behandelt typische Laufzeitfehler, die bewusst als "Bad Request" bewertet werden sollen.
     *
     * <p>
     * Dazu zählen beispielsweise ungültige Statuswerte oder Validierungsfälle, die nicht über Bean Validation laufen.
     * </p>
     *
     * @param ex Ausnahme.
     * @return HTTP 400 mit Fehlerdetails.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        Map<String, Object> body = buildErrorBody(HttpStatus.BAD_REQUEST, ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    /**
     * Fallback für alle nicht gesondert behandelten Fehler.
     *
     * <p>
     * Wichtig: {@link NoResourceFoundException} wird oben separat behandelt und fällt daher nicht in diesen Handler.
     * </p>
     *
     * @param ex Ausnahme.
     * @return HTTP 500 mit Fehlerdetails.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {
        Map<String, Object> body = buildErrorBody(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    /**
     * Hilfsmethode zum Erstellen der Error-Response-Body.
     *
     * @param status HTTP-Status
     * @param message Fehlermeldung
     * @return Map mit Fehlerdetails
     */
    private Map<String, Object> buildErrorBody(HttpStatus status, String message) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", OffsetDateTime.now().toString());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        return body;
    }
}