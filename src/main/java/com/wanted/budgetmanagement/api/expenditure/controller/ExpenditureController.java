package com.wanted.budgetmanagement.api.expenditure.controller;

import com.wanted.budgetmanagement.api.expenditure.dto.ExpenditureCreateRequest;
import com.wanted.budgetmanagement.api.expenditure.dto.ExpenditureListResponse;
import com.wanted.budgetmanagement.api.expenditure.dto.ExpenditureUpdateRequest;
import com.wanted.budgetmanagement.api.expenditure.service.ExpenditureService;
import com.wanted.budgetmanagement.domain.user.entity.User;
import com.wanted.budgetmanagement.global.exception.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/expenditures")
@Tag(name = "Expenditures", description = "Expenditures API")
public class ExpenditureController {

    private final ExpenditureService expenditureService;

    @Operation(summary = "Expenditure 생성 API", responses = {
            @ApiResponse(responseCode = "201")
    })
    @Tag(name = "Expenditures")
    @PostMapping
    public ResponseEntity expenditureCreate(@Validated @RequestBody ExpenditureCreateRequest request, @AuthenticationPrincipal User user) {
        expenditureService.expenditureCreate(request, user);

        return ResponseEntity.created(URI.create("/api/expenditures")).body(new BaseResponse<>(201, "지출 생성에 성공했습니다."));
    }

    @Operation(summary = "Expenditures 수정 API", responses = {
            @ApiResponse(responseCode = "200")
    })
    @Tag(name = "Expenditures")
    @PatchMapping("/{expenditureId}")
    public ResponseEntity expenditureUpdate(@PathVariable Long expenditureId, @Validated @RequestBody ExpenditureUpdateRequest request, @AuthenticationPrincipal User user) {
        expenditureService.expenditureUpdate(expenditureId, request, user);

        return ResponseEntity.ok().body(new BaseResponse<>(200, "지출 수정에 성공했습니다."));
    }

    @Operation(summary = "Expenditures 목록 조회 API", responses = {
            @ApiResponse(responseCode = "200")
    })
    @Tag(name = "Expenditures")
    @GetMapping
    public ResponseEntity expenditureList(@RequestParam LocalDate minPeriod, @RequestParam LocalDate maxPeriod,
                                       @RequestParam String categoryName, @RequestParam long minMoney,
                                       @RequestParam long maxMoney, @AuthenticationPrincipal User user) {
        ExpenditureListResponse listResponse = expenditureService.expenditureList(minPeriod, maxPeriod, categoryName, minMoney, maxMoney, user);

        return ResponseEntity.ok().body(new BaseResponse<>(200, "지출 목록 조회에 성공했습니다.", listResponse));
    }
}