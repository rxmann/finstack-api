package com.app.budgets.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class MetricCard {
    private BigDecimal current;
    private BigDecimal previous;
    private Trend trend;
    private String message;
    private Integer percentageChange;
}
