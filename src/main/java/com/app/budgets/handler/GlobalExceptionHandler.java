package com.app.budgets.handler;

import com.app.budgets.handler.exceptions.UserAlreadyExistsException;
import com.app.budgets.handler.exceptions.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashSet;
import java.util.Set;

import static com.app.budgets.handler.ErrorCodes.BAD_CREDENTIALS;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Handle authentication failures (unauthenticated users)
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ExceptionResponse> handleAuthenticationException(AuthenticationException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(
                        ExceptionResponse.builder()
                                .errorCode(401)
                                .errorDescription("Authentication Failed")
                                .error(ex.getMessage())
                                .build());
    }

    // Handle access denied (authenticated but not authorized)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionResponse> handleAccessDeniedException(AccessDeniedException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(
                        ExceptionResponse.builder()
                                .errorCode(403)
                                .errorDescription("Access Denied")
                                .error(ex.getMessage())
                                .build());
    }

    // Handle validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleException(MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        Set<String> errors = new HashSet<>();

        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.add(error.getField() + ": " + error.getDefaultMessage()));

        ex.getBindingResult().getGlobalErrors()
                .forEach(error -> errors.add(error.getObjectName() + ": " + error.getDefaultMessage()));

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
        log.error(ex.getMessage());
        return ResponseEntity
                .status(ErrorCodes.USER_NOT_FOUND.getStatusCode())
                .body(
                        ExceptionResponse.builder()
                                .errorCode(ErrorCodes.USER_NOT_FOUND.getCode())
                                .errorDescription(ErrorCodes.USER_NOT_FOUND.getDescription())
                                .error(ex.getMessage())
                                .build());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ExceptionResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        var rootMessage = ex.getMostSpecificCause().getMessage();
        ErrorCodes errorCode = ErrorCodes.INVALID_INPUT;
        var cleanDescription = "A data integrity constraint was violated.";

        if (rootMessage != null) {
            rootMessage = rootMessage.toLowerCase();

            if (rootMessage.contains("duplicate key")) {
                errorCode = ErrorCodes.DUPLICATE_NAME;
                cleanDescription = "Entity with the same unique field already exists.";
            } else if (rootMessage.contains("foreign key")) {
                cleanDescription = "Referenced entity does not exist or is invalid.";
            } else if (rootMessage.contains("not-null")) {
                cleanDescription = "A required field was missing or null.";
            } else if (rootMessage.contains("check constraint")) {
                cleanDescription = "A field value failed a database constraint check.";
            }
        }

        return ResponseEntity
                .status(errorCode.getStatusCode())
                .body(ExceptionResponse.builder()
                        .errorCode(errorCode.getCode())
                        .errorDescription(cleanDescription)
                        .error(rootMessage)
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
