package com.wanted.budgetmanagement.global.jwt;

import lombok.Getter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.Assert;

@Getter
public class JwtAuthenticationException extends AuthenticationException {

    private final JwtError error;

    public JwtAuthenticationException(JwtError error, String message) {
        super(message);
        Assert.notNull(error, "error must not be null");
        this.error = error;
    }

}
