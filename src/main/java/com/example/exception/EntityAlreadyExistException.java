package com.example.exception;

public class EntityAlreadyExistException extends RuntimeException {

    private final String resourceName;
    private final Long resourceId;

    public EntityAlreadyExistException(String resourceName, Long resourceId) {
        super(resourceName + " not found with id: " + resourceId);
        this.resourceName = resourceName;
        this.resourceId = resourceId;
    }

    // free text constructor
    public EntityAlreadyExistException(String message) {
        super(message);
        this.resourceName = null;
        this.resourceId = null;
    }

    public String getResourceName() { return resourceName; }
    public Long getResourceId() { return resourceId; }
}