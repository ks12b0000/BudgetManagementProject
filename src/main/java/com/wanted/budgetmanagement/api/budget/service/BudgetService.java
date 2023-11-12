package com.wanted.budgetmanagement.api.budget.service;

import com.wanted.budgetmanagement.api.budget.dto.BudgetSettingRequest;
import com.wanted.budgetmanagement.domain.budget.entity.Budget;
import com.wanted.budgetmanagement.domain.budget.repository.BudgetRepository;
import com.wanted.budgetmanagement.domain.budgetCategory.entity.BudgetCategory;
import com.wanted.budgetmanagement.domain.budgetCategory.repository.BudgetCategoryRepository;
import com.wanted.budgetmanagement.domain.user.entity.User;
import com.wanted.budgetmanagement.global.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.wanted.budgetmanagement.global.exception.BaseExceptionStatus.NON_EXISTENT_CATEGORY;

@Service
@RequiredArgsConstructor
public class BudgetService {

    private final BudgetRepository budgetRepository;

    private final BudgetCategoryRepository categoryRepository;

    /**
     * request에서 받은 categoryName으로 카테고리를 조회 후 존재하지 않은 카테고리면 예외처리하고,
     * request에서 받은 값들을 저장한다.
     * @param request : money, categoryName, period
     * @param user
     */
    public void budgetSetting(BudgetSettingRequest request, User user) {
        BudgetCategory category = categoryRepository.findByName(request.getCategoryName()).orElseThrow(() -> new BaseException(NON_EXISTENT_CATEGORY));
        Budget budget = Budget.builder()
                .category(category)
                .money(request.getMoney())
                .period(request.getPeriod())
                .user(user)
                .build();

        budgetRepository.save(budget);
    }
}
