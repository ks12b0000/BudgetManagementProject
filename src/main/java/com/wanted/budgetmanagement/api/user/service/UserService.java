package com.wanted.budgetmanagement.api.user.service;

import com.wanted.budgetmanagement.api.user.dto.UserSignUpRequest;
import com.wanted.budgetmanagement.domain.user.entity.User;
import com.wanted.budgetmanagement.domain.user.repository.UserRepository;
import com.wanted.budgetmanagement.global.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.wanted.budgetmanagement.global.exception.BaseExceptionStatus.DUPLICATE_EMAIL;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder encoder;

    /**
     * 유저 회원가입
     * 중복된 email인지 확인 후 입력 받은 password를 암호화 해서 유저를 저장한다.
     * @param request : email, password
     */
    @Transactional
    public void userSignUp(UserSignUpRequest request) {
        emailDuplicateCheck(request.getEmail());

        String enPassword = encoder.encode(request.getPassword());

        User user = User.builder()
                .email(request.getEmail())
                .password(enPassword)
                .build();

        userRepository.save(user);
    }

    /**
     * 중복된 이메일 이라면 예외를 던져주고, 그렇지 않다면 false를 리턴합니다.
     */
    private boolean emailDuplicateCheck(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new BaseException(DUPLICATE_EMAIL);
        }
        return false;
    }
}
