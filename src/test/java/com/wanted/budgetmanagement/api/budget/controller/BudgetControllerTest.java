package com.wanted.budgetmanagement.api.budget.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.wanted.budgetmanagement.api.budget.dto.BudgetSettingRequest;
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

import java.time.YearMonth;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class BudgetControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private BudgetCategoryRepository categoryRepository;

    @DisplayName("예산 설정 성공")
    @Test
    @WithMockUser
    void budgetSetting() throws Exception {
        // given
        YearMonth yearMonth = YearMonth.parse("2023-11");
        BudgetCategory category = new BudgetCategory(1L, "식비");
        categoryRepository.save(category);
        BudgetSettingRequest request = new BudgetSettingRequest(100000, "식비", yearMonth);
        String content = new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(request);

        // when
        ResultActions resultActions = mvc.perform(post("/api/budgets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        );

        // then
        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("code").value("201"))
                .andExpect(jsonPath("message").value("예산 설정에 성공했습니다."));

    }

    @DisplayName("예산 설정 실패")
    @Test
    @WithMockUser
    void budgetSettingFail() throws Exception {
        // given
        YearMonth yearMonth = YearMonth.parse("2023-11");
        BudgetSettingRequest request = new BudgetSettingRequest(100000, "핸드폰비", yearMonth);
        String content = new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(request);

        // when
        ResultActions resultActions = mvc.perform(post("/api/budgets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        );

        // then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("code").value("400"))
                .andExpect(jsonPath("message").value("존재하지 않는 카테고리입니다."));

    }

    @DisplayName("예산 추천 성공")
    @Test
    @WithMockUser
    void budgetRecommend() throws Exception {
        // given
        long totalAmount = 1000000L;

        // when
        ResultActions resultActions = mvc.perform(get("/api/budgets/recommend?totalAmount=" + totalAmount));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("code").value("200"))
                .andExpect(jsonPath("message").value("예산 추천에 성공했습니다."));

    }
}