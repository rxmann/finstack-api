package com.app.budgets.budget.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class BudgetRequest {
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    @Digits(integer = 13, fraction = 2, message = "Amount must have max 13 integer and 2 decimal digits")
    private BigDecimal amount;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    @NotNull(message = "Budget date is required")
    private LocalDateTime budgetDate;

    @Size(max = 500, message = "Receipt URL must not exceed 500 characters")
    private String receiptUrl;

    private List<@NotBlank(message = "Tag cannot be blank") String> tags;

    @NotBlank(message = "Budget category ID is required")
    private String budgetCategoryId;
}
