package com.iu.require4testing.exception;

/**
 * Exception, die geworfen wird, wenn eine angeforderte Ressource nicht gefunden wurde.
 */
public class ResourceNotFoundException extends RuntimeException {
    
    private String resourceName;
    private String fieldName;
    private Object fieldValue;
    
    /**
     * Konstruktor mit Nachricht.
     * 
     * @param message Die Fehlermeldung
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
    
    /**
     * Konstruktor mit Ressourcendetails.
     * 
     * @param resourceName Name der Ressource
     * @param fieldName Name des Feldes
     * @param fieldValue Wert des Feldes
     */
    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s nicht gefunden mit %s: '%s'", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
    
    public String getResourceName() {
        return resourceName;
    }
    
    public String getFieldName() {
        return fieldName;
    }
    
    public Object getFieldValue() {
        return fieldValue;
    }
}
