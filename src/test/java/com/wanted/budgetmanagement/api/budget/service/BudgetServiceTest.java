package com.wanted.budgetmanagement.api.budget.service;

import com.wanted.budgetmanagement.api.budget.dto.BudgetSettingRequest;
import com.wanted.budgetmanagement.domain.budget.repository.BudgetRepository;
import com.wanted.budgetmanagement.domain.budgetCategory.entity.BudgetCategory;
import com.wanted.budgetmanagement.domain.budgetCategory.repository.BudgetCategoryRepository;
import com.wanted.budgetmanagement.domain.user.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@Transactional
@ExtendWith(MockitoExtension.class)
class BudgetServiceTest {

    @InjectMocks
    private BudgetService budgetService;

    @Mock
    private BudgetRepository budgetRepository;

    @Mock
    private BudgetCategoryRepository categoryRepository;

    @DisplayName("예산 설정 성공")
    @Test
    void budgetSetting() {
        // given
        BudgetCategory category = new BudgetCategory(1L, "식비");
        BudgetSettingRequest request = new BudgetSettingRequest(100000, "식비", new Date(202311));
        User user = new User(1L, "email@gmail.com", "password", null);

        // stub
        when(categoryRepository.findByName(request.getCategoryName())).thenReturn(Optional.of(category));

        // when
        budgetService.budgetSetting(request, user);
    }

    @DisplayName("예산 설정 실패")
    @Test
    void budgetSettingFail() {
        // given
        BudgetSettingRequest request = new BudgetSettingRequest(100000, "식비", new Date(202311));
        User user = new User(1L, "email@gmail.com", "password", null);

        // stub
        // when
        // then
        assertThatThrownBy(() -> budgetService.budgetSetting(request, user)).hasMessage("존재하지 않는 카테고리입니다.");

    }

}