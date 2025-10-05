package com.app.budgets.budget.mapper;

import com.app.budgets.budget.dto.BudgetRequest;
import com.app.budgets.budget.dto.BudgetResponse;
import com.app.budgets.budget.model.Budget;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BudgetMapper {
    Budget toEntity(BudgetRequest request);

    BudgetResponse toResponse(Budget budget);
}
