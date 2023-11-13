package com.wanted.budgetmanagement.api.expenditure.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.wanted.budgetmanagement.api.expenditure.dto.ExpenditureCreateRequest;
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

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class ExpenditureControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private BudgetCategoryRepository categoryRepository;

    @DisplayName("존재하지 않은 예산으로 인한 지출 생성 실패")
    @Test
    @WithMockUser
    void expenditureCreateFail() throws Exception {
        // given
        LocalDate date = LocalDate.parse("2023-11-11");
        BudgetCategory category = new BudgetCategory(1L, "식비");
        categoryRepository.save(category);
        ExpenditureCreateRequest request = new ExpenditureCreateRequest(20000L, "식비", date, "저녁값 지출");
        String content = new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(request);

        // when
        ResultActions resultActions = mvc.perform(post("/api/expenditures")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        );

        // then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("code").value("400"))
                .andExpect(jsonPath("message").value("존재하지 않는 예산입니다."));

    }

    @DisplayName("존재하지 않은 카테고리로 인한 지출 생성 실패")
    @Test
    @WithMockUser
    void expenditureCreateFail2() throws Exception {
        // given
        LocalDate date = LocalDate.parse("2023-11-11");
        ExpenditureCreateRequest request = new ExpenditureCreateRequest(20000L, "핸드폰비", date, "저녁값 지출");
        String content = new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(request);

        // when
        ResultActions resultActions = mvc.perform(post("/api/expenditures")
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
}