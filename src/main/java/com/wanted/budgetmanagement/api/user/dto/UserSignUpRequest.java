package com.wanted.budgetmanagement.api.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserSignUpRequest {

    @Schema(description = "이메일", example = "example@gmail.com")
    @NotBlank(message = "이메일을 입력하세요.")
    @Email
    private String email;

    @Schema(description = "비밀번호", example = "example12345")
    @NotBlank(message = "비밀번호를 입력하세요.")
    private String password;
}
