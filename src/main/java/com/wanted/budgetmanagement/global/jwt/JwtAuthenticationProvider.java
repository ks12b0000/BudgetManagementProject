package com.wanted.budgetmanagement.global.jwt;

import com.wanted.budgetmanagement.domain.user.entity.User;
import com.wanted.budgetmanagement.domain.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;


public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    public JwtAuthenticationProvider(UserRepository userRepository, JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {

        var bearerToken = (BearerAuthenticationToken) authentication;
        Jws<Claims> jws;
        User user;
        try {
            jws = jwtProvider.parse(bearerToken.getToken());
            // refreshToken 만료시간 확인
            user = userRepository.findById(Long.parseLong(jws.getPayload().getSubject()))
                    .orElseThrow(() -> new JwtException("존재하지 않는 회원의 JWT"));
            jwtProvider.parse(user.getRefresh_token());
        } catch (JwtException e) {
            JwtError error = new JwtError("invalid_token", HttpStatus.BAD_REQUEST);
            throw new JwtAuthenticationException(error, "유효하지 않은 JWT");
        }

        var auth = new JwtAuthenticationToken(jws, user.getAuthorities());
        auth.setPrincipal(user);

        return auth;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return BearerAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
