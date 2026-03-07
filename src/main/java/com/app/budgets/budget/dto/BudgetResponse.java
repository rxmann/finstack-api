package com.app.budgets.budget.dto;

import com.app.budgets.budget.model.BudgetType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class BudgetResponse {
    private String id;
    private String name;
    private BigDecimal amount;
    private BudgetType budgetType;
    private String budgetCategoryName;
    private String budgetCategoryId;
    private LocalDate budgetDate;
}
