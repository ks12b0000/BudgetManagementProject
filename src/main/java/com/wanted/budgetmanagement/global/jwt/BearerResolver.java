package com.wanted.budgetmanagement.global.jwt;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class BearerResolver {

    private static final Pattern authorizationPattern = Pattern.compile("^Bearer (?<token>[a-zA-Z0-9-._~+/]+)=*$");


    public String resolve(HttpServletRequest request) {
        return resolveFromAuthorizationHeader(request);
    }

    private String resolveFromAuthorizationHeader(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (StringUtils.hasText(authorization) && authorization.startsWith("Bearer")) {
            Matcher matcher = authorizationPattern.matcher(authorization);

            if ( !matcher.matches() ) {
                JwtError error = new JwtError("invalid_request", HttpStatus.BAD_REQUEST);
                throw new JwtAuthenticationException(error, "토큰 형식 오류");
            }

            return matcher.group("token");
        }
        return null;
    }

}
