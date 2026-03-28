package com.app.budgets.dashboard.dto.metric;

import com.app.budgets.budget.model.BudgetType;

import java.math.BigDecimal;

public interface BudgetComposition {
   String getCategory();
   BudgetType getBudgetType();
   BigDecimal getAmount();
   Double getAmountPct();
}