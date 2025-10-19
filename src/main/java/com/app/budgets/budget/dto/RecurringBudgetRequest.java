package com.app.budgets.budget.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.app.budgets.budget.model.BudgetType;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RecurringBudgetRequest {

    @NotNull
    private String budgetCategoryId;

    @NotNull
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    private BigDecimal amount;

    private String description;

    @NotNull
    private BudgetType budgetType;

    @NotBlank
    private String frequency; // DAILY | WEEKLY | MONTHLY | YEARLY

    private Integer frequencyInterval = 1;

    @NotNull
    private LocalDate startDate;

    private LocalDate endDate;

    private LocalDate nextOccurrence;
    private LocalDate createdAt;

}
