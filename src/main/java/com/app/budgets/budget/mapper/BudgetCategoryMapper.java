package com.app.budgets.budget.mapper;

import com.app.budgets.budget.dto.BudgetCategoryRequest;
import com.app.budgets.budget.dto.BudgetCategoryResponse;
import com.app.budgets.budget.model.BudgetCategory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BudgetCategoryMapper {
    BudgetCategory toEntity(BudgetCategoryRequest request);

    BudgetCategoryResponse toResponse(BudgetCategory category);
}
