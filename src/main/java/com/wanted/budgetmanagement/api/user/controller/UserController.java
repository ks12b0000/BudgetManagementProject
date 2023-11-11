package com.wanted.budgetmanagement.api.user.controller;

import com.wanted.budgetmanagement.api.user.dto.UserSignInResponse;
import com.wanted.budgetmanagement.api.user.dto.UserSignInRequest;
import com.wanted.budgetmanagement.api.user.dto.UserSignUpRequest;
import com.wanted.budgetmanagement.api.user.service.UserService;
import com.wanted.budgetmanagement.global.exception.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @Operation(summary = "User 로그인 API", responses = {
            @ApiResponse(responseCode = "200")
    })
    @Tag(name = "Users")
    @PostMapping("/signin")
    public ResponseEntity userSignIn(@Validated @RequestBody UserSignInRequest request) {
        UserSignInResponse response = userService.userSignIn(request);

        return ResponseEntity.ok().body(new BaseResponse(200, "로그인에 성공했습니다.", response));
    }

}
