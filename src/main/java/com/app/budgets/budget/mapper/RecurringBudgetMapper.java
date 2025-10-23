package com.app.budgets.budget.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.app.budgets.budget.dto.RecurringBudgetRequest;
import com.app.budgets.budget.dto.RecurringBudgetResponse;
import com.app.budgets.budget.model.RecurringBudget;

@Mapper(componentModel = "spring")
public interface RecurringBudgetMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "budgetCategory", ignore = true)
    @Mapping(target = "nextOccurrence", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "lastModifiedAt", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    RecurringBudget toEntity(RecurringBudgetRequest request);

    @Mapping(target = "budgetCategoryId", source = "budgetCategory.id")
    @Mapping(target = "userId", source = "user.id")
    RecurringBudgetResponse toResponse(RecurringBudget recurringBudget);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "budgetCategory", ignore = true)
    @Mapping(target = "nextOccurrence", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "lastModifiedAt", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    void updateEntity(RecurringBudgetRequest request, @MappingTarget RecurringBudget recurringBudget);
}
