package com.app.budgets.dashboard.dto.response;

import com.app.budgets.dashboard.dto.Trend;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record MetricCard(
        BigDecimal current,
        BigDecimal previous,
        Trend trend,
        String message,
        Integer percentageChange,
        BigDecimal net
) {
}
