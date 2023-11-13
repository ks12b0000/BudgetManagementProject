package com.wanted.budgetmanagement.api.Expenditure.controller;

import com.wanted.budgetmanagement.api.Expenditure.dto.ExpenditureCreateRequest;
import com.wanted.budgetmanagement.api.Expenditure.service.ExpenditureService;
import com.wanted.budgetmanagement.domain.user.entity.User;
import com.wanted.budgetmanagement.global.exception.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/expenditures")
@Tag(name = "Expenditures", description = "Expenditures API")
public class ExpenditureController {

    private final ExpenditureService expenditureService;

    @Operation(summary = "Expenditure 생성 API", responses = {
            @ApiResponse(responseCode = "201")
    })
    @Tag(name = "Expenditures")
    @PostMapping
    public ResponseEntity expenditureCreate(@Validated @RequestBody ExpenditureCreateRequest request, @AuthenticationPrincipal User user) {
        expenditureService.expenditureCreate(request, user);

        return ResponseEntity.created(URI.create("/api/expenditures")).body(new BaseResponse<>(201, "지출 생성에 성공했습니다."));
    }
}
