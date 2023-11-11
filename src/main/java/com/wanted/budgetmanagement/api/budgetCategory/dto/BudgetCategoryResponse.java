package com.wanted.budgetmanagement.api.budgetCategory.dto;

import com.wanted.budgetmanagement.domain.budgetCategory.entity.BudgetCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BudgetCategoryResponse {

    List<BudgetCategory> categories;
}
