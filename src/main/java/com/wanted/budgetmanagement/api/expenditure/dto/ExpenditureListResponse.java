package com.wanted.budgetmanagement.api.expenditure.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ExpenditureListResponse {

    private List<ExpenditureList> expenditureLists;

    private long viewMoneyTotal;

    private long totalCategoryMoneyTotal;
}
