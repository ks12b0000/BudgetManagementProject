package com.wanted.budgetmanagement.api.expenditure.dto;

import com.wanted.budgetmanagement.domain.budgetCategory.entity.BudgetCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class ExpenditureGuideResponse {

    private List<ExpenditureGuide> guideList;

    private long totalAmount;
}
