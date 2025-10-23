package com.app.budgets.budget.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

@Data
public class RecurringBudgetResponse {
    private String id;
    private String userId;
    private String budgetCategoryId;
    private BigDecimal amount;
    private String name;
    private String description;
    private String frequency;
    private Integer frequencyInterval;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate nextOccurrence;
}
