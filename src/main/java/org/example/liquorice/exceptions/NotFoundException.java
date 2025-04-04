package org.example.liquorice.exceptions;

public class NotFoundException extends RuntimeException {
    
    public NotFoundException(String message) {
        super(message);
    }
    
    public NotFoundException(String entityType, String id) {
        super(entityType + " with id " + id + " not found");
    }
}
