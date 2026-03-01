package com.app.budgets.dashboard.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record RecurringMetricCard(
        Integer totalCount,
        BigDecimal totalSum,
        String message
) {}