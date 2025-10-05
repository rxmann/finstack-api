package com.app.budgets.budget.dto;


import com.app.budgets.budget.model.BudgetType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class BudgetRequest {
    private String name;
    private BigDecimal amount;
    private BudgetType type;
    private Long categoryId;
    private LocalDate startDate;
    private LocalDate endDate;
}
