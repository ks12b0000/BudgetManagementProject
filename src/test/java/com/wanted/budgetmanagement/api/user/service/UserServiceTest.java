package com.wanted.budgetmanagement.api.user.service;

import com.wanted.budgetmanagement.api.user.dto.UserSignInRequest;
import com.wanted.budgetmanagement.api.user.dto.UserSignInResponse;
import com.wanted.budgetmanagement.api.user.dto.UserSignUpRequest;
import com.wanted.budgetmanagement.domain.user.entity.User;
import com.wanted.budgetmanagement.domain.user.repository.UserRepository;
import com.wanted.budgetmanagement.global.jwt.JwtProvider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@Transactional
@ExtendWith(MockitoExtension.class)
class UserServiceTest {


    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private JwtProvider jwtProvider;

    @DisplayName("유저 회원가입 성공")
    @Test
    void userSignUp() {
        // given
        UserSignUpRequest request = new UserSignUpRequest("email@gmail.com", "password12");

        // when
        userService.userSignUp(request);
    }

    @DisplayName("유저 회원가입 실패")
    @Test
    void userSignUpFail() {
        // given
        UserSignUpRequest request = new UserSignUpRequest("email@gmail.com", "password12");

        // stub
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);
        // when

        // then
        assertThatThrownBy(() -> userService.userSignUp(request)).hasMessage("중복된 이메일이 있습니다.");
    }

    @DisplayName("유저 로그인 성공")
    @Test
    void userSignIn() {
        // given
        User user = new User(1L, "email@gmail.com", encoder.encode("password12"), null);
        UserSignInRequest request = new UserSignInRequest("email@gmail.com", "password12");

        // stub
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(encoder.matches(request.getPassword(), user.getPassword())).thenReturn(true);

        // when
        userService.userSignIn(request);

        // then
    }

    @DisplayName("유저 로그인 실패")
    @Test
    void userSignInFail() {
        /**
         * 유저 이메일을 찾지 못해서 로그인 실패.
         */
        // given
        User user = new User(1L, "email@gmail.com", encoder.encode("password12"), null);
        UserSignInRequest request = new UserSignInRequest("email@gmail.com", "password12");

        // when
        // then
        assertThatThrownBy(() -> userService.userSignIn(request)).hasMessage("존재하지 않는 유저입니다.");

        /**
         * 유저 패스워드가 일치하지 않아서 로그인 실패.
         */
        // stub
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));

        // then
        assertThatThrownBy(() -> userService.userSignIn(request)).hasMessage("아이디 또는 비밀번호가 일치하지 않습니다.");
    }
}