package com.wanted.budgetmanagement.api.expenditure.service;

import com.wanted.budgetmanagement.api.expenditure.dto.ExpenditureCreateRequest;
import com.wanted.budgetmanagement.api.expenditure.dto.ExpenditureList;
import com.wanted.budgetmanagement.api.expenditure.dto.ExpenditureListResponse;
import com.wanted.budgetmanagement.api.expenditure.dto.ExpenditureUpdateRequest;
import com.wanted.budgetmanagement.domain.budget.entity.Budget;
import com.wanted.budgetmanagement.domain.budget.repository.BudgetRepository;
import com.wanted.budgetmanagement.domain.budgetCategory.entity.BudgetCategory;
import com.wanted.budgetmanagement.domain.budgetCategory.repository.BudgetCategoryRepository;
import com.wanted.budgetmanagement.domain.expenditure.entity.Expenditure;
import com.wanted.budgetmanagement.domain.expenditure.repository.ExpenditureRepository;
import com.wanted.budgetmanagement.domain.user.entity.User;
import com.wanted.budgetmanagement.global.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static com.wanted.budgetmanagement.global.exception.BaseExceptionStatus.*;

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

    /**
     * 지출 수정
     * money, memo, category, period을 수정한다.
     * request에서 받은 categoryName으로 카테고리를 조회 후 존재하지 않은 카테고리면 예외 발생
     * 존재하지 않는 expenditureId가 들어오면 예외 발생,
     * 수정할 지출의 유저와 다를경우 예외 발생
     * @param expenditureId
     * @param request : money, memo, category, period
     * @param user
     */
    @Transactional
    public void expenditureUpdate(Long expenditureId, ExpenditureUpdateRequest request, User user) {
        Expenditure expenditure = expenditureRepository.findById(expenditureId).orElseThrow(() -> new BaseException(NON_EXISTENT_EXPENDITURE));
        BudgetCategory category = categoryRepository.findByName(request.getCategoryName()).orElseThrow(() -> new BaseException(NON_EXISTENT_CATEGORY));
        if (expenditure.getUser().getId() != user.getId()) {
            throw new BaseException(FORBIDDEN_USER);
        }
        expenditure.updateExpenditure(request, category);
    }

    /**
     * 지출 목록 조회
     * 기간, 카테고리, 최소, 최대 금액으로 지출 목록을 조회한다.
     * 조회된 모든 내용의 지출 합계, 카테고리별 지출 합계를 같이 반환합니다.
     * request에서 받은 categoryName으로 카테고리를 조회 후 존재하지 않은 카테고리면 예외 발생
     * @param minPeriod
     * @param maxPeriod
     * @param categoryName
     * @param minMoney
     * @param maxMoney
     * @param user
     * @return
     */
    @Transactional(readOnly = true)
    public ExpenditureListResponse expenditureList(LocalDate minPeriod, LocalDate maxPeriod,
                                String categoryName, long minMoney, long maxMoney, User user) {
        BudgetCategory category = categoryRepository.findByName(categoryName).orElseThrow(() -> new BaseException(NON_EXISTENT_CATEGORY));
        List<ExpenditureList> expenditureList = expenditureRepository.findByExpenditureList(minPeriod, maxPeriod, category, user, minMoney, maxMoney);
        long viewMoneyTotal = expenditureRepository.findByViewMoneyTotal(minPeriod, maxPeriod, category, user, minMoney, maxMoney);
        long totalCategoryMoneyTotal = expenditureRepository.findByTotalCategoryMoneyTotal(category, user);

        return new ExpenditureListResponse(expenditureList, viewMoneyTotal, totalCategoryMoneyTotal);
    }
}