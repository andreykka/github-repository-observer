package com.vcs.observer.exception;

public class UserNotFoundException extends ResourceNotFoundException {
    private static final long serialVersionUID = 8346329385559931820L;
    private static final String USER_NOT_FOUND_MESSAGE_FORMAT = "User:'%s' is not found";

    public UserNotFoundException(String username) {
        super(String.format(USER_NOT_FOUND_MESSAGE_FORMAT, username));
    }
}
