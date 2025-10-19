package com.app.budgets.budget.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.budgets.budget.dto.RecurringBudgetRequest;
import com.app.budgets.budget.dto.RecurringBudgetResponse;
import com.app.budgets.budget.mapper.RecurringBudgetMapper;
import com.app.budgets.budget.repository.BudgetCategoryRepository;
import com.app.budgets.budget.repository.RecurringBudgetRepository;
import com.app.budgets.user.UserAuth;
import com.app.budgets.user.UserRepository;

@Service
@Transactional
public class RecurringBudgetService {

    private final RecurringBudgetRepository recurringBudgetRepository;
    private final UserRepository userRepository;
    private final BudgetCategoryRepository budgetCategoryRepository;
    private final RecurringBudgetMapper recurringBudgetMapper;
    private final UserAuth userAuth;

    public RecurringBudgetService(RecurringBudgetRepository recurringBudgetRepository,
            UserRepository userRepository,
            BudgetCategoryRepository budgetCategoryRepository,
            RecurringBudgetMapper recurringBudgetMapper,
            UserAuth userAuth) {
        this.recurringBudgetRepository = recurringBudgetRepository;
        this.userRepository = userRepository;
        this.budgetCategoryRepository = budgetCategoryRepository;
        this.recurringBudgetMapper = recurringBudgetMapper;
        this.userAuth = userAuth;
    }

    public RecurringBudgetResponse createRecurringBudget(RecurringBudgetRequest request) {

        var authUser = userAuth.getCurrentUser();

        var user = userRepository.findById(authUser.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        var category = budgetCategoryRepository.findById(request.getBudgetCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Budget category not found"));

        var recurringBudget = recurringBudgetMapper.toEntity(request);
        recurringBudget.setUser(user);
        recurringBudget.setBudgetCategory(category);

        var saved = recurringBudgetRepository.save(recurringBudget);
        return recurringBudgetMapper.toResponse(saved);
    }
}
