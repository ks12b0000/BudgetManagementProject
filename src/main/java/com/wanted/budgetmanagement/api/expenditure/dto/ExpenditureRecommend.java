package com.wanted.budgetmanagement.api.expenditure.dto;

import com.wanted.budgetmanagement.domain.budgetCategory.entity.BudgetCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class ExpenditureRecommend {

    private BudgetCategory category;

    private long todayExpenditurePossibleMoney;

}
