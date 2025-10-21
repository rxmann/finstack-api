package com.app.budgets.budget.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.app.budgets.budget.dto.BudgetRequest;
import com.app.budgets.budget.dto.BudgetResponse;
import com.app.budgets.budget.model.Budget;

@Mapper(componentModel = "spring")
public interface BudgetMapper {
    Budget toEntity(BudgetRequest request);

    @Mapping(target = "budgetCategoryName", source = "budgetCategory.name")
    @Mapping(target = "budgetCategoryId", source = "budgetCategory.id")
    @Mapping(target = "budgetType", source = "budgetCategory.budgetType")
    BudgetResponse toResponse(Budget budget);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "lastModifiedAt", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    void updateEntity(BudgetRequest request, @MappingTarget Budget budget);
}
