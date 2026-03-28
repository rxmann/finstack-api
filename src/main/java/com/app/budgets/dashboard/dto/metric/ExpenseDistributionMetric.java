package com.app.budgets.dashboard.dto.metric;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record ExpenseDistributionMetric(
        String periodStr,
        String category,
        BigDecimal amount,
        LocalDate period
) {}