package br.com.atlantico.versioncontrolapi.exception;

import javax.naming.AuthenticationException;

public class InvalidJwtAuthenticationException extends AuthenticationException {
    public InvalidJwtAuthenticationException(String msg) {
        super(msg);
    }
}
