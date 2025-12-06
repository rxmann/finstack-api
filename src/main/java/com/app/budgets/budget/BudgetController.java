package com.app.budgets.budget;

import com.app.budgets.budget.dto.BudgetRequest;
import com.app.budgets.budget.dto.BudgetResponse;
import com.app.budgets.budget.service.BudgetService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/budgets")
@RequiredArgsConstructor
@Tag(name = "Budget", description = "budget management endpoints")
public class BudgetController {

    private final BudgetService budgetService;

    @PostMapping
    public ResponseEntity<BudgetResponse> createBudget(@Valid @RequestBody BudgetRequest request) {
        return ResponseEntity.ok(budgetService.createBudget(request));
    }

    @GetMapping
    public ResponseEntity<List<BudgetResponse>> getAllBudgets(Pageable pageable) {
        return ResponseEntity.ok(budgetService.getAllBudgets(pageable));
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
