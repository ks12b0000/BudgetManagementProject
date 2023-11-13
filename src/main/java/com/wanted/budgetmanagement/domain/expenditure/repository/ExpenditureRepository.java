package com.wanted.budgetmanagement.domain.expenditure.repository;

import com.wanted.budgetmanagement.api.expenditure.dto.ExpenditureList;
import com.wanted.budgetmanagement.domain.budgetCategory.entity.BudgetCategory;
import com.wanted.budgetmanagement.domain.expenditure.entity.Expenditure;
import com.wanted.budgetmanagement.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ExpenditureRepository extends JpaRepository<Expenditure,Long> {

    @Query("select new com.wanted.budgetmanagement.api.expenditure.dto.ExpenditureList(" +
            "memo, period, category, excludingTotal, money) " +
            "from Expenditure " +
            "where category = :category AND period BETWEEN :minPeriod AND :maxPeriod " +
            "AND user = :user AND money >= :minMoney AND money <= :maxMoney")
    List<ExpenditureList> findByExpenditureList(@Param("minPeriod") LocalDate minPeriod, @Param("maxPeriod") LocalDate maxPeriod,
                                                @Param("category") BudgetCategory category, @Param("user") User user,
                                                @Param("minMoney") long minMoney, @Param("maxMoney") long maxMoney);

    @Query("select ifNull(sum(money), 0) " +
            "from Expenditure " +
            "where category = :category AND period BETWEEN :minPeriod AND :maxPeriod " +
            "AND user = :user AND money >= :minMoney AND money <= :maxMoney AND excludingTotal = false")
    long findByViewMoneyTotal(@Param("minPeriod") LocalDate minPeriod, @Param("maxPeriod") LocalDate maxPeriod,
                              @Param("category") BudgetCategory category, @Param("user") User user,
                              @Param("minMoney") long minMoney, @Param("maxMoney") long maxMoney);

    @Query("select ifNull(sum(money), 0) " +
            "from Expenditure " +
            "where category = :category AND user = :user AND excludingTotal = false")
    long findByTotalCategoryMoneyTotal(@Param("category") BudgetCategory category, @Param("user") User user);
}
