package com.wanted.budgetmanagement.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum BaseExceptionStatus {
;
    private final HttpStatus code;
    private final String message;
}
