package com.vcs.observer.client.exception;

/**
 * Exception to be thrown in case of Internal Server Error of VCS
 */
public class VcsClientInternalServerException extends VcsClientException {
    private static final long serialVersionUID = 1142817333584058115L;
    public static final String DEFAULT_INTERNAL_SERVER_ERROR_MESSAGE = "VCS system internal server error";

    public VcsClientInternalServerException(String message) {
        super(message);
    }

    public VcsClientInternalServerException() {
        this(DEFAULT_INTERNAL_SERVER_ERROR_MESSAGE);
    }
}
