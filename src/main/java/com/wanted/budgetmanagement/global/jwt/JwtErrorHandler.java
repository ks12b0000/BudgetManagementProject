package com.wanted.budgetmanagement.global.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Setter
public class JwtErrorHandler {

    private String realmName;

    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            HttpStatus httpStatus) {

        this.handle(response, authParamAttributes(), httpStatus);
    }

    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            HttpStatus httpStatus, String error, String description, String uri) {

        Map<String, String> authParamAttributes = authParamAttributes();

        authParamAttributes.put("error", error);

        if (description != null) {
            authParamAttributes.put("error_description", description);
        }

        if (uri != null) {
            authParamAttributes.put("error_uri", uri);
        }

        this.handle(response, authParamAttributes, httpStatus);
    }

    private Map<String, String> authParamAttributes() {
        Map<String, String> authParamAttributes = new LinkedHashMap<>();

        if ( this.realmName != null ) {
            authParamAttributes.put("realm", this.realmName);
        }

        return authParamAttributes;
    }

    private void handle(
            HttpServletResponse response,
            Map<String, String> authParamAttributes,
            HttpStatus httpStatus) {

        String wwwAuthenticate = "Bearer";
        if (!authParamAttributes.isEmpty()) {
            wwwAuthenticate += authParamAttributes.entrySet().stream()
                    .map(attribute -> attribute.getKey() + "=\"" + attribute.getValue() + "\"")
                    .collect(Collectors.joining(", ", " ", ""));
        }
        response.addHeader(HttpHeaders.WWW_AUTHENTICATE, wwwAuthenticate);
        response.setStatus(httpStatus.value());
    }

}
