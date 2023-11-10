package com.wanted.budgetmanagement.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum BaseExceptionStatus {

    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "중복된 이메일이 있습니다.");

    private final HttpStatus code;
    private final String message;
}
