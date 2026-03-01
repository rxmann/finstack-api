package com.app.budgets.dashboard.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface CashFlow {
   String getDateRange();
   BigDecimal getIncomeAmount();
   BigDecimal getExpenseAmount();
   LocalDate getPeriod();
}