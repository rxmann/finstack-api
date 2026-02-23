package com.app.budgets.budget.dto;

import java.math.BigDecimal;

public interface RecurringMetrics {
    BigDecimal getCount();

    BigDecimal getSum();
}