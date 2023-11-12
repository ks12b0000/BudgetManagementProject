package com.wanted.budgetmanagement.api.budget.service;

import com.wanted.budgetmanagement.api.budget.dto.BudgetSettingRequest;
import com.wanted.budgetmanagement.api.budget.dto.BudgetUpdateRequest;
import com.wanted.budgetmanagement.domain.budget.entity.Budget;
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

import static org.assertj.core.api.Assertions.assertThat;
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

    @DisplayName("예산 수정 성공")
    @Test
    void budgetUpdate() {
        // given
        User user = new User(1L, "email@gmail.com", "password", null);
        BudgetCategory category = new BudgetCategory(1L, "식비");
        Budget budget = new Budget(1L, user, category, 10000, new Date(202311));

        BudgetUpdateRequest request = new BudgetUpdateRequest(1000002);

        // stub
        when(budgetRepository.findById(budget.getId())).thenReturn(Optional.of(budget));

        // when
        budgetService.budgetUpdate(budget.getId(), request, user);

        // then
        assertThat(budget.getMoney()).isEqualTo(request.getMoney());
    }

    @DisplayName("존재하지 않는 예산으로 인한 수정 실패")
    @Test
    void budgetUpdateFail() {
        // given
        User user = new User(1L, "email@gmail.com", "password", null);
        BudgetCategory category = new BudgetCategory(1L, "식비");
        Budget budget = new Budget(1L, user, category, 10000, new Date(202311));

        BudgetUpdateRequest request = new BudgetUpdateRequest(1000002);

        // stub
        // when
        // then
        assertThatThrownBy(() -> budgetService.budgetUpdate(budget.getId(), request, user)).hasMessage("존재하지 않는 예산입니다.");
    }

    @DisplayName("수정할 예산의 유저와 다른 유저로 인한 수정 실패")
    @Test
    void budgetUpdateFail2() {
        // given
        User user = new User(1L, "email@gmail.com", "password", null);
        BudgetCategory category = new BudgetCategory(1L, "식비");
        Budget budget = new Budget(1L, user, category, 10000, new Date(202311));
        User failUser = new User(2L, "email2@gmail.com", "password", null);
        BudgetUpdateRequest request = new BudgetUpdateRequest(1000002);

        // stub
        when(budgetRepository.findById(budget.getId())).thenReturn(Optional.of(budget));

        // when
        // then
        assertThatThrownBy(() -> budgetService.budgetUpdate(budget.getId(), request, failUser)).hasMessage("권한이 없는 유저입니다.");
    }

}