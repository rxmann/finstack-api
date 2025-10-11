package com.app.budgets.budget;

import java.util.List;

import com.app.budgets.budget.service.BudgetService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.app.budgets.budget.dto.BudgetCategoryRequest;
import com.app.budgets.budget.dto.BudgetCategoryResponse;
import com.app.budgets.budget.dto.BudgetRequest;
import com.app.budgets.budget.dto.BudgetResponse;
import com.app.budgets.budget.service.BudgetCategoryService;

import lombok.RequiredArgsConstructor;

@Slf4j
@RestController
@RequestMapping("/budgets")
@RequiredArgsConstructor
public class BudgetController {

    private final BudgetService budgetService;
    private final BudgetCategoryService budgetCategoryService;

    // ----- Budget Categories -----
    @PostMapping("/categories")
    public ResponseEntity<BudgetCategoryResponse> createCategory(@Valid @RequestBody BudgetCategoryRequest request) {
        return ResponseEntity.ok(budgetCategoryService.createCategory(request));
    }

    @GetMapping("/categories")
    public ResponseEntity<List<BudgetCategoryResponse>> getAllCategories() {
        return ResponseEntity.ok(budgetCategoryService.getAllCategories());
    }

    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<BudgetCategoryResponse> getCategoryById(@PathVariable String categoryId) {
        return ResponseEntity.ok(budgetCategoryService.getCategoryById(categoryId));
    }

    @PutMapping("/categories/{categoryId}")
    public ResponseEntity<BudgetCategoryResponse> updateCategory(
            @PathVariable String categoryId,
            @Valid @RequestBody BudgetCategoryRequest request) {
        return ResponseEntity.ok(budgetCategoryService.updateCategory(categoryId, request));
    }

    @DeleteMapping("/categories/{categoryId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable String categoryId) {
        budgetCategoryService.deleteCategory(categoryId);
        return ResponseEntity.noContent().build();
    }

    // ----- Budgets -----
    @PostMapping
    public ResponseEntity<BudgetResponse> createBudget(@RequestBody BudgetRequest request) {
        return ResponseEntity.ok(budgetService.createBudget(request));
    }

    @GetMapping
    public ResponseEntity<List<BudgetResponse>> getAllBudgets() {
        return ResponseEntity.ok(budgetService.getAllBudgets());
    }

    @GetMapping("/{budgetId}")
    public ResponseEntity<BudgetResponse> getBudgetById(@PathVariable String budgetId) {
        return ResponseEntity.ok(budgetService.getBudgetById(budgetId));
    }

    @PutMapping("/{budgetId}")
    public ResponseEntity<BudgetResponse> updateBudget(
            @PathVariable String budgetId,
            @Valid @RequestBody BudgetRequest request) {
        return ResponseEntity.ok(budgetService.updateBudget(budgetId, request));
    }

    @DeleteMapping("/{budgetId}")
    public ResponseEntity<Void> deleteBudget(@PathVariable String budgetId) {
        budgetService.deleteBudget(budgetId);
        return ResponseEntity.noContent().build();
    }

}
