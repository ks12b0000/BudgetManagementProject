package com.wanted.budgetmanagement.api.budget.service;

import com.wanted.budgetmanagement.api.budget.dto.BudgetRecommendListResponse;
import com.wanted.budgetmanagement.api.budget.dto.BudgetRecommendResponse;
import com.wanted.budgetmanagement.api.budget.dto.BudgetSettingRequest;
import com.wanted.budgetmanagement.api.budget.dto.BudgetUpdateRequest;
import com.wanted.budgetmanagement.domain.budget.entity.Budget;
import com.wanted.budgetmanagement.domain.budget.repository.BudgetRepository;
import com.wanted.budgetmanagement.domain.budgetCategory.entity.BudgetCategory;
import com.wanted.budgetmanagement.domain.budgetCategory.repository.BudgetCategoryRepository;
import com.wanted.budgetmanagement.domain.user.entity.User;
import com.wanted.budgetmanagement.global.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.wanted.budgetmanagement.global.exception.BaseExceptionStatus.*;

@Service
@RequiredArgsConstructor
public class BudgetService {

    private final BudgetRepository budgetRepository;

    private final BudgetCategoryRepository categoryRepository;

    /**
     * 예산 설정
     * request에서 받은 categoryName으로 카테고리를 조회 후 존재하지 않은 카테고리면 예외처리하고,
     * request에서 받은 값들을 저장한다.
     * @param request : money, categoryName, period
     * @param user
     */
    @Transactional
    public void budgetSetting(BudgetSettingRequest request, User user) {
        BudgetCategory category = categoryRepository.findByName(request.getCategoryName()).orElseThrow(() -> new BaseException(NON_EXISTENT_CATEGORY));
        existsByBudget(request, user, category);

        Budget budget = Budget.builder()
                .category(category)
                .money(request.getMoney())
                .period(request.getPeriod())
                .user(user)
                .build();

        budgetRepository.save(budget);
    }

    /**
     * 이미 설정한 예산이라면 예외처리.
     * @param request
     * @param user
     * @param category
     */
    private void existsByBudget(BudgetSettingRequest request, User user, BudgetCategory category) {
        Budget exists = budgetRepository.findByCategoryAndPeriodAndUser(category, request.getPeriod(), user);
        if (exists != null) {
            throw new BaseException(DUPLICATE_BUDGET);
        }
    }

    /**
     * 예산 수정
     * budgetId, money, user를 받아서 예산을 수정한다.
     * 만약 없는 budgetId가 들어오면 예외 발생, 수정할 예산의 유저와 다를경우 예외 발생
     * @param budgetId
     * @param request : money
     * @param user
     */
    @Transactional
    public void budgetUpdate(Long budgetId, BudgetUpdateRequest request, User user) {
        Budget budget = budgetRepository.findById(budgetId).orElseThrow(() -> new BaseException(NON_EXISTENT_BUDGET));
        if (budget.getUser().getId() != user.getId()) {
            throw new BaseException(FORBIDDEN_USER);
        }
        budget.updateBudget(request.getMoney());
    }

    /**
     * 예산 추천
     * totalAmount를 기존 이용중인 유저들이 설정한 평균값으로 카테고리별로 적정 금액을 나눠서 반환한다.
     * @param totalAmount
     * @return
     */
    @Transactional(readOnly = true)
    public BudgetRecommendListResponse budgetRecommend(long totalAmount) {
        List<BudgetRecommendResponse> responseList = budgetRepository.findByAverage(totalAmount);

        return new BudgetRecommendListResponse(responseList);
    }
}
