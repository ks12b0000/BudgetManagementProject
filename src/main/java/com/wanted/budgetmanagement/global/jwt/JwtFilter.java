package com.wanted.budgetmanagement.global.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;

    private final AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource =
            new WebAuthenticationDetailsSource();

    private final BearerResolver bearerResolver = new BearerResolver();

    private final AuthenticationEntryPoint authenticationEntryPoint = new JwtEntryPoint();

    public JwtFilter(AuthenticationManager authenticationManager) {
        Assert.notNull(authenticationManager, "authenticationManager 는 필수입니다.");
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        final boolean debug = this.logger.isDebugEnabled();

        String token;

        try {
            token = this.bearerResolver.resolve(request);
        } catch (AuthenticationException invalid) {
            this.authenticationEntryPoint.commence(request, response, invalid);
            return;
        }

        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        BearerAuthenticationToken authenticationRequest = new BearerAuthenticationToken(token);

        authenticationRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));

        try {
            Authentication authenticationResult = this.authenticationManager.authenticate(authenticationRequest);

            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authenticationResult);
            SecurityContextHolder.setContext(context);

            filterChain.doFilter(request, response);
        } catch (AuthenticationException failed) {
            SecurityContextHolder.clearContext();

            if (debug) {
                this.logger.debug("인증작업 실패: " + failed);
            }

            this.authenticationEntryPoint.commence(request, response, failed);
        }
    }
}
