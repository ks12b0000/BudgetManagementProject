package com.wanted.budgetmanagement.api.budgetCategory.service;

import com.wanted.budgetmanagement.api.budgetCategory.dto.BudgetCategoryResponse;
import com.wanted.budgetmanagement.domain.budgetCategory.entity.BudgetCategory;
import com.wanted.budgetmanagement.domain.budgetCategory.repository.BudgetCategoryRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@Transactional
@ExtendWith(MockitoExtension.class)
class BudgetCategoryServiceTest {

    @InjectMocks
    private BudgetCategoryService categoryService;

    @Mock
    private BudgetCategoryRepository categoryRepository;

    @DisplayName("예산 카테고리 목록 조회")
    @Test
    void categoryList() {
        // given
        BudgetCategory category = new BudgetCategory(1L, "식비");
        List<BudgetCategory> list = new ArrayList<>();
        list.add(category);

        // stub
        when(categoryRepository.findAll()).thenReturn(list);
        // when
        BudgetCategoryResponse response = categoryService.categoryList();

        // then
        assertThat(response.getCategories().get(0).getName()).isEqualTo("식비");
    }

}