package com.app.budgets.budget.scheduler;

import com.app.budgets.budget.mapper.RecurringBudgetMapper;
import com.app.budgets.budget.model.RecurringBudget;
import com.app.budgets.budget.repository.RecurringBudgetRepository;
import com.app.budgets.budget.service.BudgetService;
import com.app.budgets.common.enums.BudgetFrequency;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static com.app.budgets.budget.service.RecurringBudgetService.getLocalDate;

@Service
@Slf4j
public class RecurringBudgetScheduler {
    private final RecurringBudgetRepository recurringBudgetRepository;
    private final BudgetService budgetService;

    public RecurringBudgetScheduler(RecurringBudgetRepository recurringBudgetRepository, BudgetService budgetService, RecurringBudgetMapper recurringBudgetMapper) {
        this.recurringBudgetRepository = recurringBudgetRepository;
        this.budgetService = budgetService;
    }

    @Scheduled(cron = "10 0 0 * * *")
//    @Scheduled(initialDelay = 1000, fixedDelay = 10000)
    public void processRecurringBudgets() {

        log.info("Processing recurring budgets");
        LocalDate today = LocalDate.now();
        List<RecurringBudget> dueBudgets = recurringBudgetRepository.findDueRecurringBudgets(today);

        dueBudgets.forEach(rBudget -> {
            try {
                log.info("Processing recurring budget: {}", rBudget.getId());

                // 1 Create actual budget for today
                budgetService.createBudgetFromRecurring(rBudget);

                // 2 Calculate next occurrence
                LocalDate next = calculateNextOccurrence(rBudget.getNextOccurrence(), rBudget.getFrequency());
                rBudget.setNextOccurrence(next);

                // 3 Save update
                recurringBudgetRepository.save(rBudget);

            } catch (Exception e) {
                log.error("Failed to process recurring budget {}: {}", rBudget.getId(), e.getMessage(), e);
            }
        });
    }

    private LocalDate calculateNextOccurrence(LocalDate fromDate, BudgetFrequency frequency) {
        if (fromDate == null) return null;

        return getLocalDate(fromDate, frequency);
    }


}
