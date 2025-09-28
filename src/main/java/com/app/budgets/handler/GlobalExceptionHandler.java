package com.app.budgets.handler;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.app.budgets.handler.exceptions.UserAlreadyExistsException;
import com.app.budgets.handler.exceptions.UserNotFoundException;

import jakarta.servlet.http.HttpServletRequest;
import static com.app.budgets.handler.ErrorCodes.BAD_CREDENTIALS;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Handle validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleException(MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        Set<String> errors = new HashSet<>();
        ex.getBindingResult().getAllErrors()
                .forEach(err -> errors.add(err.getDefaultMessage()));

        var expResponse = ExceptionResponse.builder()
                .errorCode(ErrorCodes.INVALID_INPUT.getCode())
                .errorDescription(ErrorCodes.INVALID_INPUT.getDescription())
                .validationErrors(errors)
                .build();

        return ResponseEntity.status(ErrorCodes.INVALID_INPUT.getStatusCode()).body(expResponse);
    }

    // Handle bad credentials
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionResponse> handleException() {
        return ResponseEntity
                .status(BAD_CREDENTIALS.getStatusCode())
                .body(
                        ExceptionResponse.builder()
                                .errorCode(BAD_CREDENTIALS.getCode())
                                .errorDescription(BAD_CREDENTIALS.getDescription())
                                .error("Login and/or Password is incorrect")
                                .build());
    }

    // Handle user already exists
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ExceptionResponse> handleException(UserAlreadyExistsException ex) {
        return ResponseEntity
                .status(ErrorCodes.USER_EXISTS.getStatusCode())
                .body(
                        ExceptionResponse.builder()
                                .errorCode(ErrorCodes.USER_EXISTS.getCode())
                                .errorDescription(ErrorCodes.USER_EXISTS.getDescription())
                                .error(ex.getMessage())
                                .build());
    }

    // Handle user not found
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleException(UserNotFoundException ex) {
        return ResponseEntity
                .status(ErrorCodes.USER_NOT_FOUND.getStatusCode())
                .body(
                        ExceptionResponse.builder()
                                .errorCode(ErrorCodes.USER_NOT_FOUND.getCode())
                                .errorDescription(ErrorCodes.USER_NOT_FOUND.getDescription())
                                .error(ex.getMessage())
                                .build());
    }

    // Catch-all handler
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(Exception ex, HttpServletRequest request) {
        log.error("Unexpected error", ex);

        var expResponse = ExceptionResponse.builder()
                .errorCode(500)
                .errorDescription("Internal Server Error")
                .error(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(expResponse);
    }
}
