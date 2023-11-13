package com.wanted.budgetmanagement.api.Expenditure.service;

import com.wanted.budgetmanagement.api.Expenditure.dto.ExpenditureCreateRequest;
import com.wanted.budgetmanagement.domain.budgetCategory.entity.BudgetCategory;
import com.wanted.budgetmanagement.domain.budgetCategory.repository.BudgetCategoryRepository;
import com.wanted.budgetmanagement.domain.expenditure.entity.Expenditure;
import com.wanted.budgetmanagement.domain.expenditure.repository.ExpenditureRepository;
import com.wanted.budgetmanagement.domain.user.entity.User;
import com.wanted.budgetmanagement.global.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.wanted.budgetmanagement.global.exception.BaseExceptionStatus.NON_EXISTENT_CATEGORY;

@Service
@RequiredArgsConstructor
public class ExpenditureService {

    private final ExpenditureRepository expenditureRepository;

    private final BudgetCategoryRepository categoryRepository;

    /**
     * 지출 생성
     * request에서 받은 categoryName으로 카테고리를 조회 후 존재하지 않은 카테고리면 예외처리하고,
     * request에서 받은 값들을 저장한다.
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
    }
}
