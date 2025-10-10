package com.app.budgets.budget.dto;

import java.time.LocalDate;

import com.app.budgets.budget.model.BudgetType;

import lombok.Data;

@Data
public class BudgetRequest {
    private String name;
    private Double amount;
    private BudgetType type;
    private String budgetCategoryId;
    private LocalDate startDate;
    private LocalDate endDate;
}
