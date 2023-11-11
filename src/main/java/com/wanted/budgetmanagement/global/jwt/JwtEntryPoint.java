package com.wanted.budgetmanagement.global.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class JwtEntryPoint implements AuthenticationEntryPoint {

    private final JwtErrorHandler handler = new JwtErrorHandler();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        if (authException instanceof JwtAuthenticationException e) {

            JwtError error = e.getError();

            this.handler.handle(
                    request,
                    response,
                    error.getHttpStatus(), error.getErrorCode(),
                    error.getDescription(), error.getUri());
        } else {
            this.handler.handle(request, response, HttpStatus.UNAUTHORIZED);
        }
    }

}
