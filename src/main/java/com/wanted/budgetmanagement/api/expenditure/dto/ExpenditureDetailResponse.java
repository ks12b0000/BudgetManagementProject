package com.wanted.budgetmanagement.api.expenditure.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ExpenditureDetailResponse {

    private String memo;

    private LocalDate period;

    private String categoryName;

    private boolean excludingTotal;

    private long money;
}
