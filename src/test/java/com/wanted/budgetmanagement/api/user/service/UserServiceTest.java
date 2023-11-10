package com.wanted.budgetmanagement.api.user.service;

import com.wanted.budgetmanagement.api.user.dto.UserSignUpRequest;
import com.wanted.budgetmanagement.domain.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

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
}