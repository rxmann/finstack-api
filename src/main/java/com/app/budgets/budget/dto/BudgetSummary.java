package com.app.budgets.budget.dto;

import java.math.BigDecimal;

public interface BudgetSummary {
    BigDecimal getPreviousIncome();
    BigDecimal getPreviousExpense();
    BigDecimal getCurrentIncome();
    BigDecimal getCurrentExpense();
}