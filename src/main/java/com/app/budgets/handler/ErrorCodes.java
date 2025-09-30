package com.app.budgets.handler;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

public enum ErrorCodes {

    BAD_CREDENTIALS(100, "Login and/or password is incorrect", UNAUTHORIZED),
    INVALID_INPUT(101, "Invalid input data", BAD_REQUEST),
    USER_EXISTS(102, "User already exists", BAD_REQUEST),
    USER_NOT_FOUND(103, "User not found with the given details", NOT_FOUND);

    @Getter
    private final int code;
    @Getter
    private final String description;
    @Getter
    private final HttpStatus statusCode;

    private ErrorCodes(int code, String description, HttpStatus statusCode) {
        this.code = code;
        this.description = description;
        this.statusCode = statusCode;
    }

}
