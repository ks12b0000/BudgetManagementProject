package com.wanted.budgetmanagement.api.user.controller;

import com.wanted.budgetmanagement.api.user.dto.UserSignUpRequest;
import com.wanted.budgetmanagement.api.user.service.UserService;
import com.wanted.budgetmanagement.global.exception.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Tag(name = "Users", description = "Users API")
public class UserController {

    private final UserService userService;

    @Operation(summary = "User 회원가입 API", responses = {
            @ApiResponse(responseCode = "201")
    })
    @Tag(name = "Users")
    @PostMapping
    public ResponseEntity userSignUp(@Validated @RequestBody UserSignUpRequest request) {
        userService.userSignUp(request);

        return ResponseEntity.created(URI.create("/api/users")).body(new BaseResponse(201, "유저 회원가입에 성공했습니다."));
    }

}
