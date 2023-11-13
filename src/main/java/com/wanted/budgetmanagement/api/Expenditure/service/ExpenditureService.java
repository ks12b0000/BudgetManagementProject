package com.wanted.budgetmanagement.api.Expenditure.service;

import com.wanted.budgetmanagement.api.Expenditure.dto.ExpenditureCreateRequest;
import com.wanted.budgetmanagement.domain.budget.entity.Budget;
import com.wanted.budgetmanagement.domain.budget.repository.BudgetRepository;
import com.wanted.budgetmanagement.domain.budgetCategory.entity.BudgetCategory;
import com.wanted.budgetmanagement.domain.budgetCategory.repository.BudgetCategoryRepository;
import com.wanted.budgetmanagement.domain.expenditure.entity.Expenditure;
import com.wanted.budgetmanagement.domain.expenditure.repository.ExpenditureRepository;
import com.wanted.budgetmanagement.domain.user.entity.User;
import com.wanted.budgetmanagement.global.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;

import static com.wanted.budgetmanagement.global.exception.BaseExceptionStatus.NON_EXISTENT_BUDGET;
import static com.wanted.budgetmanagement.global.exception.BaseExceptionStatus.NON_EXISTENT_CATEGORY;

@Service
@RequiredArgsConstructor
public class ExpenditureService {

    private final ExpenditureRepository expenditureRepository;

    private final BudgetCategoryRepository categoryRepository;

    private final BudgetRepository budgetRepository;

    /**
     * 지출 생성
     * request에서 받은 categoryName으로 카테고리를 조회 후 존재하지 않은 카테고리면 예외처리하고,
     * request에서 받은 값들을 저장하고,
     * 지정된 카테고리 예산에서 마이너스 해준다.
     * @param request : money, memo, category, period
     * @param user
     */
    @Transactional
    public void expenditureCreate(ExpenditureCreateRequest request, User user) {
        BudgetCategory category = categoryRepository.findByName(request.getCategoryName()).orElseThrow(() -> new BaseException(NON_EXISTENT_CATEGORY));
        Expenditure expenditure = Expenditure.builder()
                .money(request.getMoney())
                .memo(request.getMemo())
                .category(category)
                .period(request.getPeriod())
                .user(user)
                .build();

        expenditureRepository.save(expenditure);

        LocalDate date = LocalDate.of(request.getPeriod().getYear(), request.getPeriod().getMonth(), 1);
        Budget budget = budgetRepository.findByCategoryAndPeriodAndUser(category, date, user);

        if (budget == null) {
            throw new BaseException(NON_EXISTENT_BUDGET);
        }

        budget.updateBudget(budget.getMoney() - request.getMoney());
    }
}
