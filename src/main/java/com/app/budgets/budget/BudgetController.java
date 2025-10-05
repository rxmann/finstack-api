package com.app.budgets.budget;

import com.app.budgets.budget.dto.BudgetCategoryRequest;
import com.app.budgets.budget.dto.BudgetCategoryResponse;
import com.app.budgets.budget.dto.BudgetRequest;
import com.app.budgets.budget.dto.BudgetResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/budgets")
@RequiredArgsConstructor
public class BudgetController {

    private final BudgetService budgetService;
    private final BudgetCategoryService budgetCategoryService;

    // ----- Categories -----
    @PostMapping("/categories")
    public ResponseEntity<BudgetCategoryResponse> createCategory(@RequestBody BudgetCategoryRequest request) {
        return ResponseEntity.ok(budgetCategoryService.createCategory(request));
    }

    @GetMapping("/categories")
    public ResponseEntity<List<BudgetCategoryResponse>> getAllCategories() {
        return ResponseEntity.ok(budgetCategoryService.getAllCategories());
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

}
