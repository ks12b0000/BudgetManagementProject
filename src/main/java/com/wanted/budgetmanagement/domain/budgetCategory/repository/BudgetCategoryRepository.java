package com.wanted.budgetmanagement.domain.budgetCategory.repository;

import com.wanted.budgetmanagement.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BudgetCategoryRepository extends JpaRepository<User,Long> {
}
