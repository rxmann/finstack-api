package com.app.budgets.budget.dto;

import java.math.BigDecimal;
import java.time.LocalDate;


import com.app.budgets.budget.dto.groups.ValidationGroupsRecurringBudget.CreateRecurringBudget;
import com.app.budgets.budget.dto.groups.ValidationGroupsRecurringBudget.UpdateRecurringBudget;
import com.app.budgets.budget.model.BudgetType;
import com.app.budgets.common.enums.BudgetFrequency;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecurringBudgetRequest {
    @NotNull(groups = UpdateRecurringBudget.class, message = "ID is required for update")
    @Null(groups = CreateRecurringBudget.class, message = "ID must be null for create")
    private String id;

    @NotNull(message = "Budget category is required", groups = { CreateRecurringBudget.class,
            UpdateRecurringBudget.class })
    private String budgetCategoryId;

    @NotNull(message = "Amount is required", groups = { CreateRecurringBudget.class, UpdateRecurringBudget.class })
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero", groups = { CreateRecurringBudget.class,
            UpdateRecurringBudget.class })
    private BigDecimal amount;

    @NotBlank(message = "Name is required", groups = { CreateRecurringBudget.class, UpdateRecurringBudget.class })
    private String name;

    private String description;

    @NotNull(message = "Frequency is required", groups = { CreateRecurringBudget.class, UpdateRecurringBudget.class })
    private BudgetFrequency frequency;

    @NotNull(message = "Frequency interval is required", groups = { CreateRecurringBudget.class,
            UpdateRecurringBudget.class })
    @Min(value = 1, message = "Frequency interval must be at least 1", groups = { CreateRecurringBudget.class,
            UpdateRecurringBudget.class })
    private Integer frequencyInterval = 1;

    @NotNull(message = "Start date is required", groups = { CreateRecurringBudget.class, UpdateRecurringBudget.class })
    private LocalDate startDate;

    private LocalDate endDate;
}
