package com.app.budgets.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
public class RecurringMetricCard {
    private Integer totalCount;
    private BigDecimal totalSum;
    private String message;
}