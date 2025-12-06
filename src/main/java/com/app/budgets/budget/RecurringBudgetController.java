package com.app.budgets.budget;

import com.app.budgets.budget.dto.RecurringBudgetRequest;
import com.app.budgets.budget.dto.RecurringBudgetResponse;
import com.app.budgets.budget.dto.groups.ValidationGroupsRecurringBudget.CreateRecurringBudget;
import com.app.budgets.budget.dto.groups.ValidationGroupsRecurringBudget.UpdateRecurringBudget;
import com.app.budgets.budget.model.RecurringBudget;
import com.app.budgets.budget.service.RecurringBudgetService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/budgets")
@RequiredArgsConstructor
@Tag(name = "Recurring Budget", description = "Recurring Budget management endpoints")
public class RecurringBudgetController {

    private final RecurringBudgetService recurringBudgetService;

    @GetMapping("/recurring")
    public ResponseEntity<List<RecurringBudgetResponse>> getRecurringBudgets(Pageable pageable) {
        var recurringBudget = recurringBudgetService.getRecurringBudgets(pageable);
        return ResponseEntity.ok(recurringBudget);
    }

    @GetMapping("/recurring/{id}")
    public ResponseEntity<RecurringBudgetResponse> getRecurringBudget(
            @PathVariable String id) throws Exception {
        var response = recurringBudgetService.getRecurringBudget(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/recurring")
    public ResponseEntity<RecurringBudgetResponse> createRecurringBudget(
            @Validated(CreateRecurringBudget.class) @RequestBody RecurringBudgetRequest request)
            throws Exception {
        var response = recurringBudgetService.createRecurringBudget(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/recurring/{id}")
    public ResponseEntity<RecurringBudgetResponse> updateRecurringBudget(
            @PathVariable String id,
            @Validated(UpdateRecurringBudget.class) @RequestBody RecurringBudgetRequest request)
            throws Exception {
        if (!id.equals(request.getId())) {
            throw new BadRequestException("Path ID does not match request body ID");
        }
        var response = recurringBudgetService.updateRecurringBudget(request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/recurring/{id}/disable")
    public ResponseEntity<RecurringBudgetResponse> disableRecurringBudget(
            @PathVariable String id) throws Exception {
        var response = recurringBudgetService.disableRecurringBudget(id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/recurring/{id}/enable")
    public ResponseEntity<RecurringBudgetResponse> enableRecurringBudget(
            @PathVariable String id) throws Exception {
        var response = recurringBudgetService.enableRecurringBudget(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/recurring/{id}")
    public ResponseEntity<Void> deleteRecurringBudget(
            @PathVariable String id) throws Exception {
        recurringBudgetService.deleteRecurringBudget(id);
        return ResponseEntity.noContent().build();
    }

}
