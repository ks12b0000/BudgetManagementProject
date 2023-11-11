package com.wanted.budgetmanagement.global.jwt;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;

@Getter
public class JwtError {

    private HttpStatus httpStatus;

    private final String errorCode;

    private final String description;

    private final String uri;

    public JwtError(String errorCode, String description, String uri) {
        Assert.hasText(errorCode, "errorCode cannot be empty");
        this.errorCode = errorCode;
        this.description = description;
        this.uri = uri;
    }

    public JwtError(String errorCode, HttpStatus httpStatus) {
        this(errorCode, httpStatus, null, null);
    }

    public JwtError(String errorCode, HttpStatus httpStatus, String description, String errorUri) {
        this(errorCode, description, errorUri);
        Assert.notNull(httpStatus, "httpStatus must not be null");

        this.httpStatus = httpStatus;
    }

}
