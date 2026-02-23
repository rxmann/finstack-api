package com.app.budgets.budget.service;

import com.app.budgets.budget.dto.BudgetRequest;
import com.app.budgets.budget.dto.BudgetResponse;
import com.app.budgets.budget.mapper.BudgetMapper;
import com.app.budgets.budget.model.Budget;
import com.app.budgets.budget.model.RecurringBudget;
import com.app.budgets.budget.repository.BudgetCategoryRepository;
import com.app.budgets.budget.repository.BudgetRepository;
import com.app.budgets.user.UserAuth;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final BudgetCategoryRepository budgetCategoryRepository;
    private final BudgetMapper budgetMapper;
    private final UserAuth userAuth;

    @Transactional
    public BudgetResponse createBudget(BudgetRequest request) {
        var user = userAuth.getCurrentUser();
        var budget = budgetMapper.toEntity(request);
        budget.setUser(user);

        var budgetCategory = budgetCategoryRepository.findById(request.getBudgetCategoryId()).orElseThrow(() -> new IllegalArgumentException("Invalid category"));
        budget.setBudgetCategory(budgetCategory);

        var saved = budgetRepository.save(budget);
        return budgetMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<BudgetResponse> getAllBudgets(Pageable pageable) {
        var user = userAuth.getCurrentUser();
        log.info(pageable.toString());
        return budgetRepository.findAllByUserId(user.getId(), pageable).stream().map(budgetMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public BudgetResponse getBudgetById(String budgetId) {
        var user = userAuth.getCurrentUser();
        var budget = budgetRepository.findByIdAndUserId(budgetId, user.getId()).orElseThrow(() -> new IllegalArgumentException("Budget not found or access denied"));
        return budgetMapper.toResponse(budget);
    }

    @Transactional
    public BudgetResponse updateBudget(String budgetId, BudgetRequest request) {
        var user = userAuth.getCurrentUser();

        var budget = budgetRepository.findByIdAndUserId(budgetId, user.getId()).orElseThrow(() -> new IllegalArgumentException("Budget not found or access denied"));

        var budgetCategory = budgetCategoryRepository.findByIdAndUserIdAndIsActiveTrue(request.getBudgetCategoryId(), user.getId()).orElseThrow(() -> new IllegalArgumentException("Category not found or access denied"));

        budgetMapper.updateEntity(request, budget);
        budget.setBudgetCategory(budgetCategory);

        var updated = budgetRepository.save(budget);
        return budgetMapper.toResponse(updated);
    }

    @Transactional
    public void deleteBudget(String budgetId) {
        var user = userAuth.getCurrentUser();
        var budget = budgetRepository.findByIdAndUserId(budgetId, user.getId()).orElseThrow(() -> new IllegalArgumentException("Budget not found or access denied"));
        budgetRepository.delete(budget);
    }

    @Transactional
    public BudgetResponse createBudgetFromRecurring(RecurringBudget rBudget) {
        var user = rBudget.getUser();
        var category = rBudget.getBudgetCategory();

        // Map recurring budget to BudgetRequest
        BudgetRequest request = BudgetRequest.builder().amount(rBudget.getAmount()).name(rBudget.getName()).budgetDate(java.sql.Date.valueOf(rBudget.getNextOccurrence())).budgetCategoryId(category.getId()).build();

        Budget budget = Budget.builder().amount(request.getAmount()).name(request.getName()).budgetDate(LocalDateTime.now()).budgetCategory(category).user(user).receiptUrl(request.getReceiptUrl()).tags(request.getTags()).build();

        Budget saved = budgetRepository.save(budget);

        return budgetMapper.toResponse(saved);
    }
}
