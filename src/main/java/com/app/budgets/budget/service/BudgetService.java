package com.app.budgets.budget.service;

import com.app.budgets.budget.repository.BudgetCategoryRepository;
import com.app.budgets.budget.repository.BudgetRepository;
import com.app.budgets.budget.repository.RecurringBudgetRepository;
import com.app.budgets.budget.dto.BudgetRequest;
import com.app.budgets.budget.dto.BudgetResponse;
import com.app.budgets.budget.mapper.BudgetMapper;
import com.app.budgets.budget.model.RecurringBudget;
import com.app.budgets.user.UserAuth;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final RecurringBudgetRepository recurringBudgetRepository;
    private final BudgetCategoryRepository budgetCategoryRepository;
    private final BudgetMapper budgetMapper;
    private final UserAuth userAuth;

    public BudgetResponse createBudget(BudgetRequest request) {
        var user = userAuth.getCurrentUser();
        var budget = budgetMapper.toEntity(request);
        budget.setUser(user);

        var budgetCategory = budgetCategoryRepository.findById(request.getBudgetCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid category"));
        budget.setBudgetCategory(budgetCategory);

        var saved = budgetRepository.save(budget);
        return budgetMapper.toResponse(saved);
    }

    public List<BudgetResponse> getAllBudgets() {
        var user = userAuth.getCurrentUser();
        return budgetRepository.findAllByUserId(user.getId()).stream().map(budgetMapper::toResponse).toList();
    }

    public BudgetResponse getBudgetById(String budgetId) {
        var user = userAuth.getCurrentUser();
        var budget = budgetRepository.findByIdAndUserId(budgetId, user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Budget not found or access denied"));
        return budgetMapper.toResponse(budget);
    }

    public BudgetResponse updateBudget(String budgetId, BudgetRequest request) {
        var user = userAuth.getCurrentUser();

        var budget = budgetRepository.findByIdAndUserId(budgetId, user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Budget not found or access denied"));

        var budgetCategory = budgetCategoryRepository.findByIdAndUserIdAndIsActiveTrue(
                request.getBudgetCategoryId(),
                user.getId()).orElseThrow(() -> new IllegalArgumentException("Category not found or access denied"));

        budgetMapper.updateEntity(request, budget);
        budget.setBudgetCategory(budgetCategory);

        var updated = budgetRepository.save(budget);
        return budgetMapper.toResponse(updated);
    }

    public void deleteBudget(String budgetId) {
        var user = userAuth.getCurrentUser();
        var budget = budgetRepository.findByIdAndUserId(budgetId, user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Budget not found or access denied"));
        budgetRepository.delete(budget);
    }

    public List<RecurringBudget> getRecurringBudgets() {
        var user = userAuth.getCurrentUser();
        var recurringBudgets = recurringBudgetRepository.findAllByUserId(user.getId());
        return recurringBudgets;
    }
}
