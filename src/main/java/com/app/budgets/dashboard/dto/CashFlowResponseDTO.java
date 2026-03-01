package com.app.budgets.dashboard.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class CashFlowResponseDTO {
    private String dateRange;
    private LocalDate period;
    private BigDecimal incomeAmount;
    private BigDecimal expenseAmount;
}