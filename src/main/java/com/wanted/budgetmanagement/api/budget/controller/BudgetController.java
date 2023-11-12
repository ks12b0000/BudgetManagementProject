package com.wanted.budgetmanagement.api.budget.controller;

import com.wanted.budgetmanagement.api.budget.dto.BudgetSettingRequest;
import com.wanted.budgetmanagement.api.budget.service.BudgetService;
import com.wanted.budgetmanagement.domain.user.entity.User;
import com.wanted.budgetmanagement.global.exception.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/budgets")
@Tag(name = "Budgets", description = "Budgets API")
public class BudgetController {

    private final BudgetService budgetService;

    @Operation(summary = "Budget 설정 API", responses = {
            @ApiResponse(responseCode = "200")
    })
    @Tag(name = "Budgets")
    @PostMapping
    public ResponseEntity budgetSetting(@RequestBody BudgetSettingRequest request, @AuthenticationPrincipal User user) {
        budgetService.budgetSetting(request, user);

        return ResponseEntity.ok().body(new BaseResponse<>(200, "예산 설정에 성공했습니다."));
    }

}
