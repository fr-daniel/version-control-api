package br.com.atlantico.versioncontrolapi.controller;

import br.com.atlantico.versioncontrolapi.exception.VersionControlException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = VersionControlException.class)
    public ResponseEntity handleProficienciaException(VersionControlException ex) {
        return new ResponseEntity<>(ex.getMessage(), new HttpHeaders(), ex.getStatus());
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity handleFileException() {
        return new ResponseEntity("Tamanho máximo de 15MB excedido.", new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }


}
