package com.vcs.observer.exception.handler;

import com.vcs.observer.client.exception.VcsClientException;
import com.vcs.observer.exception.ResourceNotFoundException;
import com.vcs.observer.exception.model.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestControllerAdvice
public class ApplicationExceptionHandler {

    public static final String MEDIA_TYPE_NOT_SUPPORTED_FORMAT = "Media type not supported. Use:%s";
    public static final String INTERNAL_SERVER_ERROR_MESSAGE = "Internal Server Error";

    @ExceptionHandler(Exception.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ErrorResponse processUnhandledException(Exception ex) {
        log.error("Server error:{}", ex.getMessage(), ex);
        return ErrorResponse.builder()
                .status(INTERNAL_SERVER_ERROR.value())
                .message(INTERNAL_SERVER_ERROR_MESSAGE)
                .build();
    }

    @ExceptionHandler(VcsClientException.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ErrorResponse processUnhandledException(VcsClientException ex) {
        log.error("VCS client error:{}", ex.getMessage());
        return ErrorResponse.builder()
                .status(INTERNAL_SERVER_ERROR.value())
                .message(INTERNAL_SERVER_ERROR_MESSAGE)
                .build();
    }

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ResponseEntity<ErrorResponse> handleMediaTypeNotAcceptableException(HttpMediaTypeNotAcceptableException ex) {
        log.warn(ex.getMessage());
        final ErrorResponse errorBody = ErrorResponse.builder()
                .status(NOT_ACCEPTABLE.value())
                .message(String.format(MEDIA_TYPE_NOT_SUPPORTED_FORMAT, ex.getSupportedMediaTypes()))
                .build();

        // force set up Content-Type:application/json to return JSON body to the user
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE);

        return new ResponseEntity<>(errorBody, httpHeaders, NOT_ACCEPTABLE);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ErrorResponse handleResourceNotFoundException(ResourceNotFoundException ex) {
        return ErrorResponse.builder()
                .status(NOT_FOUND.value())
                .message(ex.getMessage())
                .build();
    }

}
