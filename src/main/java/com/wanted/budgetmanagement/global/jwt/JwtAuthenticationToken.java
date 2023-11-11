package com.wanted.budgetmanagement.global.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.Transient;

import java.util.Collection;

@Transient
public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final Jws<Claims> token;

    private Object credentials;

    private Object principal;

    public JwtAuthenticationToken(Jws<Claims> token) {
        super(null);
        this.token = token;
    }

    public JwtAuthenticationToken(Jws<Claims> token, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.setAuthenticated(true);
        this.token = token;
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    public void setPrincipal(Object userDetails) {
        this.principal = userDetails;
    }

    public Claims getClaims() {
        return token.getPayload();
    }

}