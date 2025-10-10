package com.app.budgets.budget.service;

import com.app.budgets.budget.repository.BudgetCategoryRepository;
import com.app.budgets.budget.repository.BudgetRepository;
import com.app.budgets.budget.dto.BudgetRequest;
import com.app.budgets.budget.dto.BudgetResponse;
import com.app.budgets.budget.mapper.BudgetMapper;
import com.app.budgets.user.UserAuth;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BudgetService {

    private final BudgetRepository budgetRepository;
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
}
