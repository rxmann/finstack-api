package com.app.budgets.budget.dto;

import com.app.budgets.budget.model.BudgetType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BudgetCategoryResponse {
    private String id;
    private String name;
    private String notes;
    private BudgetType budgetType;
    private LocalDateTime lastModifiedAt;
}
