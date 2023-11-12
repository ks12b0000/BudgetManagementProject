package com.wanted.budgetmanagement.domain.budget.repository;

import com.wanted.budgetmanagement.domain.budget.entity.Budget;
import com.wanted.budgetmanagement.domain.budgetCategory.entity.BudgetCategory;
import com.wanted.budgetmanagement.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface BudgetRepository extends JpaRepository<Budget,Long> {
    Budget findByCategoryAndPeriodAndUser(BudgetCategory category, Date period, User user);
}
