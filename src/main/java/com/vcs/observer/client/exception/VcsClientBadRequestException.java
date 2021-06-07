package com.vcs.observer.client.exception;

/**
 * Exception to be thrown in case of malformed request to VCS
 */
public class VcsClientBadRequestException extends VcsClientException {
    private static final long serialVersionUID = 853779224368844885L;

    public VcsClientBadRequestException() {
        this("VCS system Bad Request");
    }

    public VcsClientBadRequestException(String message) {
        super(message);
    }
}
