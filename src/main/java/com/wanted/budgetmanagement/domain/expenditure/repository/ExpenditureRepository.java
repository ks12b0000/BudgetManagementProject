package com.wanted.budgetmanagement.domain.expenditure.repository;

import com.wanted.budgetmanagement.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenditureRepository extends JpaRepository<User,Long> {
}
