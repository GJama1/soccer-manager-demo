package com.example.orbitaldemo.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionBody> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        logException(e);
        String message = e.getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining("\n"));
        ExceptionBody exceptionBody = new ExceptionBody();
        exceptionBody.setMessage(message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionBody);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ExceptionBody> handleResponseStatusException(ResponseStatusException e) {
        logException(e);
        ExceptionBody exceptionBody = new ExceptionBody();
        exceptionBody.setMessage(e.getReason());
        return ResponseEntity.status(e.getStatusCode()).body(exceptionBody);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionBody> handleException(Exception e) {
        logException(e);
        ExceptionBody exceptionBody = new ExceptionBody();
        exceptionBody.setMessage("Internal Server Error. Please try again later.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exceptionBody);
    }

    private void logException(Throwable e) {
        log.error("Exception ({}) has been thrown. Message: {}", e.getClass().getName(), e.getMessage());
    }

}
