package com.app.budgets.budget.dto;

import com.app.budgets.budget.model.BudgetType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
public class BudgetCategoryRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String notes;

    @NotNull
    private BudgetType budgetType = BudgetType.EXTRA;

    @NotNull
    private Boolean isActive = true;

}
