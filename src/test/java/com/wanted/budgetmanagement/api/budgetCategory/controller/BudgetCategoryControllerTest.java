package com.wanted.budgetmanagement.api.budgetCategory.controller;

import com.wanted.budgetmanagement.domain.budgetCategory.entity.BudgetCategory;
import com.wanted.budgetmanagement.domain.budgetCategory.repository.BudgetCategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class BudgetCategoryControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private BudgetCategoryRepository categoryRepository;

    @DisplayName("예산 카테고리 목록 조회")
    @Test
    @WithMockUser
    void categoryList() throws Exception {
        // given
        BudgetCategory category = new BudgetCategory(1L, "식비");
        categoryRepository.save(category);

        // when
        ResultActions resultActions = mvc.perform(get("/api/budget-categories")
                .characterEncoding("UTF-8")
        );

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("code").value("200"))
                .andExpect(jsonPath("message").value("카테고리 목록 조회에 성공했습니다."));

    }
}