package com.app.budgets.dashboard.dto.metric;

import java.math.BigDecimal;

public interface BudgetSummary {
    BigDecimal getPreviousIncome();
    BigDecimal getPreviousExpense();
    BigDecimal getCurrentIncome();
    BigDecimal getCurrentExpense();
}