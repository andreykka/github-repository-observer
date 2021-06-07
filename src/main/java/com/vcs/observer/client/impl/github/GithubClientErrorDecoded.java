package com.vcs.observer.client.impl.github;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vcs.observer.client.exception.VcsClientBadRequestException;
import com.vcs.observer.client.exception.VcsClientException;
import com.vcs.observer.client.exception.VcsClientInternalServerException;
import com.vcs.observer.client.exception.VcsClientResourceNotFoundException;
import feign.Response;
import feign.codec.Decoder;
import feign.codec.ErrorDecoder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.io.IOException;

public class GithubClientErrorDecoded implements ErrorDecoder {
    private final Decoder decoder;

    public GithubClientErrorDecoded(Decoder decoder) {
        this.decoder = decoder;
    }

    @Override
    public Exception decode(String methodKey, Response response) {
        Exception resolvedException = new VcsClientException();
        final HttpStatus responseStatus = HttpStatus.valueOf(response.status());
        if (responseStatus.is4xxClientError()) {
            resolvedException = handle4xxException(responseStatus, response);
        } else if (responseStatus.is5xxServerError()) {
            resolvedException = new VcsClientInternalServerException();
        }
        return resolvedException;
    }

    private Exception handle4xxException(HttpStatus responseStatus, Response response) {
        Exception resolvedException = null;
        if (responseStatus == HttpStatus.NOT_FOUND) {
            resolvedException = new VcsClientResourceNotFoundException();
        } else {
            try {
                final Response tempResponse = response.toBuilder().status(HttpStatus.OK.value()).build();
                final GithubErrorResponse errorResponse =
                        (GithubErrorResponse) decoder.decode(tempResponse, GithubErrorResponse.class);
                resolvedException = new VcsClientBadRequestException(errorResponse.getMessage());
            } catch (IOException e) {
                throw new IllegalStateException("Could not decode an error response from Github client");
            }
        }
        return resolvedException;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class GithubErrorResponse {
        private int status;
        private String message;
    }
}
