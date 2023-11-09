package com.wanted.budgetmanagement.domain.budget.repository;

import com.wanted.budgetmanagement.domain.budget.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BudgetRepository extends JpaRepository<Budget,Long> {
}
