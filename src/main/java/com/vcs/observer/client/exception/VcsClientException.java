package com.vcs.observer.client.exception;

/**
 * Base class for VCS client exceptions
 */
public class VcsClientException extends RuntimeException {

    private static final String VCS_CLIENT_ERROR_MESSAGE = "VCS client error";

    public VcsClientException() {
        this(VCS_CLIENT_ERROR_MESSAGE);
    }

    public VcsClientException(String message) {
        super(message);
    }
}
