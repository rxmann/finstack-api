package com.app.budgets.budget.dto;

import com.app.budgets.budget.model.BudgetType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BudgetResponse {
    private Long id;
    private String name;
    private BigDecimal amount;
    private BudgetType type;
    private String categoryName;
}
