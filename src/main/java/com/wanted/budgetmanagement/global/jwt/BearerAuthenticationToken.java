package com.wanted.budgetmanagement.global.jwt;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Transient;

@Getter
@Transient
public class BearerAuthenticationToken extends AbstractAuthenticationToken {

    private final String token;

    public BearerAuthenticationToken(String token) {
        super(null);
        this.token = token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

}
