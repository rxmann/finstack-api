package com.app.budgets.budget.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class BudgetRequest {
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    @Digits(integer = 13, fraction = 2, message = "Amount must have max 13 integer and 2 decimal digits")
    private BigDecimal amount;

    @Size(max = 50, message = "Name must not exceed 500 characters")
    private String name;

    @NotNull(message = "Budget date is required")
    private Date budgetDate;

    @Size(max = 500, message = "Receipt URL must not exceed 500 characters")
    private String receiptUrl;

    private List<@NotBlank(message = "Tag cannot be blank") String> tags;

    @NotBlank(message = "Budget category ID is required")
    private String budgetCategoryId;
}
