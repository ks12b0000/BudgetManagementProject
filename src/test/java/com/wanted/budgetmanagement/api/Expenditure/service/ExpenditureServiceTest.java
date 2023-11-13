package com.wanted.budgetmanagement.api.Expenditure.service;

import com.wanted.budgetmanagement.api.Expenditure.dto.ExpenditureCreateRequest;
import com.wanted.budgetmanagement.domain.budget.entity.Budget;
import com.wanted.budgetmanagement.domain.budget.repository.BudgetRepository;
import com.wanted.budgetmanagement.domain.budgetCategory.entity.BudgetCategory;
import com.wanted.budgetmanagement.domain.budgetCategory.repository.BudgetCategoryRepository;
import com.wanted.budgetmanagement.domain.expenditure.repository.ExpenditureRepository;
import com.wanted.budgetmanagement.domain.user.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Transactional
@ExtendWith(MockitoExtension.class)
class ExpenditureServiceTest {

    @InjectMocks
    private ExpenditureService expenditureService;

    @Mock
    private ExpenditureRepository expenditureRepository;

    @Mock
    private BudgetCategoryRepository categoryRepository;

    @Mock
    private BudgetRepository budgetRepository;

    @DisplayName("지출 생성 성공")
    @Test
    void expenditureCreate() {
        // given
        LocalDate date = LocalDate.parse("2023-11-11");
        BudgetCategory category = new BudgetCategory(1L, "식비");
        User user = new User(1L, "email@gmail.com", "password", null);
        Budget budget = new Budget(1L, user, category, 10000, date);
        ExpenditureCreateRequest request = new ExpenditureCreateRequest(20000L, "식비", date, "저녁값 지출");

        // stub
        when(categoryRepository.findByName(request.getCategoryName())).thenReturn(Optional.of(category));
        when(budgetRepository.findByCategoryAndPeriodAndUser(any(), any(), any())).thenReturn(budget);
        // when
        expenditureService.expenditureCreate(request, user);
    }

    @DisplayName("지출 생성 실패")
    @Test
    void expenditureCreateFail() {
        // given
        LocalDate date = LocalDate.parse("2023-11-11");
        User user = new User(1L, "email@gmail.com", "password", null);
        ExpenditureCreateRequest request = new ExpenditureCreateRequest(20000L, "식비", date, "저녁값 지출");

        // stub
        // when
        // then
        assertThatThrownBy(() -> expenditureService.expenditureCreate(request, user)).hasMessage("존재하지 않는 카테고리입니다.");
    }
}