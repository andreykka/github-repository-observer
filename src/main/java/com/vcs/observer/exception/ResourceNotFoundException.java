package com.vcs.observer.exception;

import java.io.Serializable;

/**
 * Base class for all the exceptions that should be translated into 404 Not Found response to the user
 */
public class ResourceNotFoundException extends RuntimeException {
    private static final long serialVersionUID = -6644732822232025572L;

    public ResourceNotFoundException() {
        this("Resource not found");
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
