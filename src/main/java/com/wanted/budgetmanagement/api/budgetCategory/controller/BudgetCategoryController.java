package com.wanted.budgetmanagement.api.budgetCategory.controller;

import com.wanted.budgetmanagement.api.budgetCategory.dto.BudgetCategoryResponse;
import com.wanted.budgetmanagement.api.budgetCategory.service.BudgetCategoryService;
import com.wanted.budgetmanagement.global.exception.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/budget-categories")
@Tag(name = "budget-category", description = "budget-category API")
public class BudgetCategoryController {

    private final BudgetCategoryService categoryService;

    @Operation(summary = "예산 카테고리 목록 조회 API", responses = {
            @ApiResponse(responseCode = "200")
    })
    @Tag(name = "budget-category")
    @GetMapping
    public ResponseEntity categoryList() {
        BudgetCategoryResponse response = categoryService.categoryList();

        return ResponseEntity.ok().body(new BaseResponse(200, "카테고리 목록 조회에 성공했습니다.", response));
    }
}
