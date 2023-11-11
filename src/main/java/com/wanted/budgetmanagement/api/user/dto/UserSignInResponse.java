package com.wanted.budgetmanagement.api.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserSignInResponse {

    private String accessToken;

    private String refreshToken;
}
