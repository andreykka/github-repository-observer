package com.vcs.observer.client.exception;

/**
 *  Exception to be thrown in case of resource was not found in VCS
 */
public class VcsClientResourceNotFoundException extends VcsClientException {

    public VcsClientResourceNotFoundException() {
        this("Resource not found");
    }

    public VcsClientResourceNotFoundException(String message) {
        super(message);
    }
}
