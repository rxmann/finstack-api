package com.app.budgets.budget;

import java.util.List;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.budgets.budget.dto.BudgetCategoryRequest;
import com.app.budgets.budget.dto.BudgetCategoryResponse;
import com.app.budgets.budget.service.BudgetCategoryService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/budgets")
@RequiredArgsConstructor
@Tag(name = "Budget Category", description = "Budget Categories management endpoints")
public class BudgetCategoryController {

    private final BudgetCategoryService budgetCategoryService;

    @PostMapping("/categories")
    public ResponseEntity<BudgetCategoryResponse> createCategory(@Valid @RequestBody BudgetCategoryRequest request) {
        return ResponseEntity.ok(budgetCategoryService.createCategory(request));
    }

    @GetMapping("/categories")
    public ResponseEntity<List<BudgetCategoryResponse>> getAllCategories(Pageable pageable) {
        return ResponseEntity.ok(budgetCategoryService.getAllCategories(pageable));
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

}
