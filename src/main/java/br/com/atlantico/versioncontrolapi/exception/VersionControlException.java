package br.com.atlantico.versioncontrolapi.exception;

import org.springframework.http.HttpStatus;

public class VersionControlException extends Exception {

    private final String message;
    private final HttpStatus status;

    public VersionControlException(String message) {
        this.message = message;
        this.status = HttpStatus.BAD_REQUEST;
    }

    public VersionControlException(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public HttpStatus getStatus() {
        return status;
    }

}
