package com.wanted.budgetmanagement.api.budget.dto;

import com.wanted.budgetmanagement.domain.budgetCategory.entity.BudgetCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BudgetRecommendResponse {

    private BudgetCategory category;

    private long average;
}
