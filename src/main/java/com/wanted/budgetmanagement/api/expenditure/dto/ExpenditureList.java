package com.wanted.budgetmanagement.api.expenditure.dto;

import com.wanted.budgetmanagement.domain.budgetCategory.entity.BudgetCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ExpenditureList {

    private String memo;

    private LocalDate period;

    private BudgetCategory category;

    private boolean excludingTotal;

    private long money;


}
