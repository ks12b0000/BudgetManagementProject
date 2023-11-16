package com.wanted.budgetmanagement.api.expenditure.service;

import com.wanted.budgetmanagement.api.expenditure.dto.*;
import com.wanted.budgetmanagement.domain.budget.entity.Budget;
import com.wanted.budgetmanagement.domain.budget.repository.BudgetRepository;
import com.wanted.budgetmanagement.domain.budgetCategory.entity.BudgetCategory;
import com.wanted.budgetmanagement.domain.budgetCategory.repository.BudgetCategoryRepository;
import com.wanted.budgetmanagement.domain.expenditure.entity.Expenditure;
import com.wanted.budgetmanagement.domain.expenditure.repository.ExpenditureRepository;
import com.wanted.budgetmanagement.domain.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Transactional
@ExtendWith(MockitoExtension.class)
class ExpenditureServiceTest {

    @InjectMocks
    private ExpenditureService expenditureService;

    @Mock
    private ExpenditureRepository expenditureRepository;

    @Mock
    private BudgetCategoryRepository categoryRepository;

    @Mock
    private BudgetRepository budgetRepository;

    @DisplayName("지출 생성 성공")
    @Test
    void expenditureCreate() {
        // given
        LocalDate date = LocalDate.parse("2023-11-11");
        BudgetCategory category = new BudgetCategory(1L, "식비");
        User user = new User(1L, "email@gmail.com", "password", null);
        Budget budget = new Budget(1L, user, category, 10000, date);
        ExpenditureCreateRequest request = new ExpenditureCreateRequest(20000L, "식비", date, "저녁값 지출");

        // stub
        when(categoryRepository.findByName(request.getCategoryName())).thenReturn(Optional.of(category));
        when(budgetRepository.findByCategoryAndPeriodAndUser(any(), any(), any())).thenReturn(budget);
        // when
        expenditureService.expenditureCreate(request, user);
    }

    @DisplayName("지출 생성 실패")
    @Test
    void expenditureCreateFail() {
        // given
        LocalDate date = LocalDate.parse("2023-11-11");
        User user = new User(1L, "email@gmail.com", "password", null);
        ExpenditureCreateRequest request = new ExpenditureCreateRequest(20000L, "식비", date, "저녁값 지출");

        // stub
        // when
        // then
        assertThatThrownBy(() -> expenditureService.expenditureCreate(request, user)).hasMessage("존재하지 않는 카테고리입니다.");
    }

    @DisplayName("지출 수정 성공")
    @Test
    void expenditureUpdate() {
        // given
        LocalDate date = LocalDate.parse("2023-11-11");
        BudgetCategory category = new BudgetCategory(2L, "교통");
        User user = new User(1L, "email@gmail.com", "password", null);
        Expenditure expenditure = new Expenditure(1L, "memo", date, category, user, false, 20000L);

        ExpenditureUpdateRequest request = new ExpenditureUpdateRequest(10000L, "교통", date, "UpdateMemo");

        // stub
        when(categoryRepository.findByName(request.getCategoryName())).thenReturn(Optional.of(category));
        when(expenditureRepository.findById(expenditure.getId())).thenReturn(Optional.of(expenditure));

        // when
        expenditureService.expenditureUpdate(expenditure.getId(), request, user);

        // then
        assertAll(
                () -> assertThat(expenditure.getMemo()).isEqualTo(request.getMemo()),
                () -> assertThat(expenditure.getCategory().getName()).isEqualTo(request.getCategoryName()),
                () -> assertThat(expenditure.getMoney()).isEqualTo(request.getMoney())
        );
    }

    @DisplayName("존재하지 않는 지출 아이디로 인한 지출 수정 실패")
    @Test
    void expenditureUpdateFail() {
        // given
        LocalDate date = LocalDate.parse("2023-11-11");
        BudgetCategory category = new BudgetCategory(2L, "교통");
        User user = new User(1L, "email@gmail.com", "password", null);
        Expenditure expenditure = new Expenditure(1L, "memo", date, category, user, false, 20000L);

        ExpenditureUpdateRequest request = new ExpenditureUpdateRequest(10000L, "교통", date, "UpdateMemo");

        // stub
        // when
        // then
        assertThatThrownBy(() -> expenditureService.expenditureUpdate(expenditure.getId(), request, user)).hasMessage("존재하지 않는 지출입니다.");
    }

    @DisplayName("존재하지 않는 카테고리 인한 지출 수정 실패")
    @Test
    void expenditureUpdateFail2() {
        // given
        LocalDate date = LocalDate.parse("2023-11-11");
        BudgetCategory category = new BudgetCategory(2L, "교통");
        User user = new User(1L, "email@gmail.com", "password", null);
        Expenditure expenditure = new Expenditure(1L, "memo", date, category, user, false, 20000L);

        ExpenditureUpdateRequest request = new ExpenditureUpdateRequest(10000L, "교통", date, "UpdateMemo");

        // stub
        when(expenditureRepository.findById(expenditure.getId())).thenReturn(Optional.of(expenditure));
        // when
        // then
        assertThatThrownBy(() -> expenditureService.expenditureUpdate(expenditure.getId(), request, user)).hasMessage("존재하지 않는 카테고리입니다.");
    }

    @DisplayName("수정할 지출의 유저와 다른 유저로 인한 지출 수정 실패")
    @Test
    void expenditureUpdateFail3() {
        // given
        LocalDate date = LocalDate.parse("2023-11-11");
        BudgetCategory category = new BudgetCategory(2L, "교통");
        User user = new User(1L, "email@gmail.com", "password", null);
        Expenditure expenditure = new Expenditure(1L, "memo", date, category, user, false, 20000L);
        User failUser = new User(2L, "email2@gmail.com", "password", null);
        ExpenditureUpdateRequest request = new ExpenditureUpdateRequest(10000L, "교통", date, "UpdateMemo");

        // stub
        when(categoryRepository.findByName(request.getCategoryName())).thenReturn(Optional.of(category));
        when(expenditureRepository.findById(expenditure.getId())).thenReturn(Optional.of(expenditure));
        // when
        // then
        assertThatThrownBy(() -> expenditureService.expenditureUpdate(expenditure.getId(), request, failUser)).hasMessage("권한이 없는 유저입니다.");
    }

    @DisplayName("지출 목록 조회 성공")
    @Test
    void expenditureList() {
        // given
        LocalDate minPeriod = LocalDate.parse("2023-11-11");
        LocalDate maxPeriod = LocalDate.parse("2023-11-23");
        BudgetCategory category = new BudgetCategory(1L, "식비");
        String categoryName = "식비";
        long minMoney = 10000L;
        long maxMoney = 20000L;
        User user = new User(1L, "email@gmail.com", "password", null);

        // stub
        when(categoryRepository.findByName(categoryName)).thenReturn(Optional.of(category));

        // when
        ExpenditureListResponse listResponse = expenditureService.expenditureList(minPeriod, maxPeriod, categoryName, minMoney, maxMoney, user);

        // then
        assertAll(
                () -> assertThat(listResponse.getExpenditureLists()).isNotNull(),
                () -> assertThat(listResponse.getViewMoneyTotal()).isNotNull(),
                () -> assertThat(listResponse.getTotalCategoryMoneyTotal()).isNotNull()
        );
    }

    @DisplayName("지출 목록 조회 실패")
    @Test
    void expenditureListFail() {
        // given
        LocalDate minPeriod = LocalDate.parse("2023-11-11");
        LocalDate maxPeriod = LocalDate.parse("2023-11-23");
        String categoryName = "식비";
        long minMoney = 10000L;
        long maxMoney = 20000L;
        User user = new User(1L, "email@gmail.com", "password", null);

        // stub
        // when
        // then
        assertThatThrownBy(() -> expenditureService.expenditureList(minPeriod, maxPeriod, categoryName, minMoney, maxMoney, user)).hasMessage("존재하지 않는 카테고리입니다.");
    }

    @DisplayName("지출 상세 조회 성공")
    @Test
    void expenditureDetail() {
        // given
        LocalDate date = LocalDate.parse("2023-11-11");
        BudgetCategory category = new BudgetCategory(2L, "교통");
        User user = new User(1L, "email@gmail.com", "password", null);
        Expenditure expenditure = new Expenditure(1L, "memo", date, category, user, false, 20000L);
        Long expenditureId = 1L;

        // stub
        when(expenditureRepository.findById(expenditureId)).thenReturn(Optional.of(expenditure));

        // when
        ExpenditureDetailResponse response = expenditureService.expenditureDetail(expenditureId, user);

        // then
        assertAll(
                () -> assertThat(response.getPeriod()).isEqualTo(expenditure.getPeriod()),
                () -> assertThat(response.getCategoryName()).isEqualTo(expenditure.getCategory().getName()),
                () -> assertThat(response.getMoney()).isEqualTo(expenditure.getMoney()),
                () -> assertThat(response.getMemo()).isEqualTo(expenditure.getMemo())
        );
    }

    @DisplayName("존재하지 않는 지출 아이디로 인한 지출 상세 조회 실패")
    @Test
    void expenditureDetailFail() {
        // given
        User user = new User(1L, "email@gmail.com", "password", null);
        Long expenditureId = 1L;

        // stub
        // when
        // then
        assertThatThrownBy(() -> expenditureService.expenditureDetail(expenditureId, user)).hasMessage("존재하지 않는 지출입니다.");
    }

    @DisplayName("조회할 지출의 유저와 다른 유저로 인한 지출 상세 조회 성공")
    @Test
    void expenditureDetailFail2() {
        // given
        LocalDate date = LocalDate.parse("2023-11-11");
        BudgetCategory category = new BudgetCategory(2L, "교통");
        User user = new User(1L, "email@gmail.com", "password", null);
        Expenditure expenditure = new Expenditure(1L, "memo", date, category, user, false, 20000L);
        Long expenditureId = 1L;
        User failUser = new User(2L, "email2@gmail.com", "password", null);

        // stub
        when(expenditureRepository.findById(expenditureId)).thenReturn(Optional.of(expenditure));

        // when
        // then
        assertThatThrownBy(() -> expenditureService.expenditureDetail(expenditureId, failUser)).hasMessage("권한이 없는 유저입니다.");

    }

    @DisplayName("지출 삭제 성공")
    @Test
    void expenditureDelete() {
        // given
        LocalDate date = LocalDate.parse("2023-11-11");
        BudgetCategory category = new BudgetCategory(2L, "교통");
        User user = new User(1L, "email@gmail.com", "password", null);
        Expenditure expenditure = new Expenditure(1L, "memo", date, category, user, false, 20000L);
        Long expenditureId = 1L;

        // stub
        when(expenditureRepository.findById(expenditureId)).thenReturn(Optional.of(expenditure));

        // when
        expenditureService.expenditureDelete(expenditureId, user);
        // then
    }

    @DisplayName("존재하지 않는 지출 아이디로 인한 지출 삭제 실패")
    @Test
    void expenditureDeleteFail() {
        // given
        User user = new User(1L, "email@gmail.com", "password", null);
        Long expenditureId = 1L;

        // stub
        // when
        // then
        assertThatThrownBy(() -> expenditureService.expenditureDelete(expenditureId, user)).hasMessage("존재하지 않는 지출입니다.");
    }

    @DisplayName("삭제할 지출의 유저와 다른 유저로 인한 지출 삭제 실패")
    @Test
    void expenditureDeleteFail2() {
        LocalDate date = LocalDate.parse("2023-11-11");
        BudgetCategory category = new BudgetCategory(2L, "교통");
        User user = new User(1L, "email@gmail.com", "password", null);
        Expenditure expenditure = new Expenditure(1L, "memo", date, category, user, false, 20000L);
        Long expenditureId = 1L;
        User failUser = new User(2L, "email2@gmail.com", "password", null);

        // stub
        when(expenditureRepository.findById(expenditureId)).thenReturn(Optional.of(expenditure));

        // when
        // then
        assertThatThrownBy(() -> expenditureService.expenditureDelete(expenditureId, failUser)).hasMessage("권한이 없는 유저입니다.");
    }

    @DisplayName("지출 합계 제외 업데이트 성공")
    @Test
    void expenditureExceptUpdate() {
        // given
        LocalDate date = LocalDate.parse("2023-11-11");
        BudgetCategory category = new BudgetCategory(2L, "교통");
        User user = new User(1L, "email@gmail.com", "password", null);
        Expenditure expenditure = new Expenditure(1L, "memo", date, category, user, false, 20000L);
        Long expenditureId = 1L;

        // stub
        when(expenditureRepository.findById(expenditureId)).thenReturn(Optional.of(expenditure));

        // when
        expenditureService.expenditureExceptUpdate(expenditureId, user, true);

        // then
        assertThat(expenditure.isExcludingTotal()).isTrue();

    }

    @DisplayName("존재하지 않는 지출 아이디로 인한 지출 합계 제외 업데이트 실패")
    @Test
    void expenditureExceptUpdateFail() {
        // given
        User user = new User(1L, "email@gmail.com", "password", null);
        Long expenditureId = 1L;

        // stub
        // when
        // then
        assertThatThrownBy(() -> expenditureService.expenditureExceptUpdate(expenditureId, user, true)).hasMessage("존재하지 않는 지출입니다.");

    }

    @DisplayName("업데이트할 지출의 유저와 다른 유저로 인한 지출 합계 제외 업데이트 실패")
    @Test
    void expenditureExceptUpdateFail2() {
        // given
        LocalDate date = LocalDate.parse("2023-11-11");
        BudgetCategory category = new BudgetCategory(2L, "교통");
        User user = new User(1L, "email@gmail.com", "password", null);
        Expenditure expenditure = new Expenditure(1L, "memo", date, category, user, false, 20000L);
        Long expenditureId = 1L;
        User failUser = new User(2L, "email2@gmail.com", "password", null);

        // stub
        when(expenditureRepository.findById(expenditureId)).thenReturn(Optional.of(expenditure));

        // then
        assertThatThrownBy(() -> expenditureService.expenditureExceptUpdate(expenditureId, failUser, true)).hasMessage("권한이 없는 유저입니다.");

    }

    @DisplayName("지출 추천 성공")
    @Test
    void expenditureRecommend() {
        // given
        LocalDate date = LocalDate.parse("2023-11-11");
        BudgetCategory category = new BudgetCategory(1L, "식비");
        User user = new User(1L, "email@gmail.com", "password", null);
        Expenditure expenditure = new Expenditure(1L, "memo", date, category, user, false, 20000L);
        LocalDate today = LocalDate.now();
        LocalDate start = today.withDayOfMonth(1);
        LocalDate end = today.withDayOfMonth(today.lengthOfMonth());
        long period = ChronoUnit.DAYS.between(today, end);

        List<ExpenditureRecommend> list = new ArrayList<>();
        list.add(new ExpenditureRecommend(expenditure.getCategory(), expenditure.getMoney()));

        // stub
        when(budgetRepository.findByExpenditureRecommend(user, start, period)).thenReturn(list);

        // when
        ExpenditureRecommendResponse response = expenditureService.expenditureRecommend(user);

        // then
        assertAll(
                () -> assertThat(response.getMessage()).isEqualTo("절약을 잘 실천하고 계시네요! 앞으로 남은 날도 절약을 위해 화이팅!"),
                () -> assertThat(response.getTodayExpenditurePossibleTotal()).isEqualTo(20000L),
                () -> assertThat(response.getRecommendList().get(0).getCategory()).isEqualTo(expenditure.getCategory())
        );

    }
}