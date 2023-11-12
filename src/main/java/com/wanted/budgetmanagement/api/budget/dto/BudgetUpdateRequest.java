package com.wanted.budgetmanagement.api.budget.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BudgetUpdateRequest {

    @Schema(description = "설정 예산", example = "100000")
    @NotNull(message = "예산을 입력해주세요.")
    private int money;

}
