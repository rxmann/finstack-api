package com.app.budgets.dashboard.dto.response;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record CashFlowResponse(String dateRange,
                               LocalDate period,
                               BigDecimal incomeAmount,
                               BigDecimal expenseAmount
) {}