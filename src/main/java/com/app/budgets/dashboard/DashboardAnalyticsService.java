package com.app.budgets.dashboard;

import com.app.budgets.budget.dto.BudgetSummary;
import com.app.budgets.budget.dto.RecurringMetrics;
import com.app.budgets.budget.model.BudgetType;
import com.app.budgets.budget.repository.BudgetRepository;
import com.app.budgets.budget.repository.RecurringBudgetRepository;
import com.app.budgets.common.enums.DateRange;
import com.app.budgets.dashboard.dto.*;
import com.app.budgets.paymentreminder.repository.PaymentReminderRepository;
import com.app.budgets.user.UserAuth;
import com.app.budgets.util.FilterUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import java.util.Set;

import static com.app.budgets.budget.model.BudgetType.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class DashboardAnalyticsService {
    private static final Set<BudgetType> INCOME_TYPES =
            Set.of(INCOME, SAVINGS, INVESTMENT, LOAN);
    private static final Set<BudgetType> EXPENSE_TYPES =
            Set.of(EXPENSE, LEND, EXTRA);
    private final BudgetRepository budgetRepository;
    private final RecurringBudgetRepository recurringBudgetRepository;
    private final PaymentReminderRepository paymentReminderRepository;
    private final UserAuth userAuth;

    public Optional<DashboardResponseDTO> getDashboardAnalytics(DashboardRequestDTO requestDTO) {
        var user = userAuth.getCurrentUser();
        log.info("Getting dashboard analytics for filter {}", requestDTO.toString());

        DateRange dateRange = FilterUtil.calculateDates(requestDTO.getFilter());

        BudgetSummary summary = budgetRepository.sumBudgetByUser(
                user.getId(),
                dateRange.startDate(),
                dateRange.endDate(),
                dateRange.prevStartDate(),
                dateRange.prevEndDate(),
                INCOME_TYPES,
                EXPENSE_TYPES
        );

        RecurringMetrics recurring = budgetRepository.getGeneratedRecurringBudget(user.getId(), dateRange.startDate(), dateRange.endDate());
        ReminderMetrics reminders = paymentReminderRepository.getReminderMetrics(user.getId());

        MetricCard incomeCard = buildMetricCard(summary.getCurrentIncome(), summary.getPreviousIncome());
        MetricCard expenseCard = buildMetricCard(summary.getCurrentExpense(), summary.getPreviousExpense());
        MetricCard recurringCard = buildRecurringCard(recurring);
        MetricCard reminderCard = buildReminderCard(reminders);

        BigDecimal net = BigDecimal.ZERO;
        if (summary.getCurrentIncome() != null) net = net.add(summary.getCurrentIncome());
        if (summary.getCurrentExpense() != null) net = net.subtract(summary.getCurrentExpense());


        return Optional.of(DashboardResponseDTO.builder()
                .income(incomeCard)
                .expense(expenseCard)
                .recurring(recurringCard)
                .net(net.intValue())
                .build());
    }


    private MetricCard buildRecurringCard(RecurringMetrics recurring) {
        BigDecimal amount = Optional.ofNullable(recurring.getSum())
                .orElse(BigDecimal.ZERO);
        BigDecimal count = Optional.ofNullable(recurring.getCount()).orElse(BigDecimal.valueOf(0));

        return MetricCard.builder()
                .current(amount)
                .message(String.format("%d payments due this period", count))
                .build();
    }

    private MetricCard buildReminderCard(ReminderMetrics reminders) {
        Long overdue = Optional.ofNullable(reminders.getOverdue()).orElse(0L);
        Long dueSoon = Optional.ofNullable(reminders.getDueSoon()).orElse(0L);

        String message;
        if (overdue > 0) {
            message = String.format("%d overdue, %d due soon", overdue, dueSoon);
        } else if (dueSoon > 0) {
            message = String.format("%d due this week", dueSoon);
        } else {
            message = "All on track";
        }

        return MetricCard.builder()
                .current(BigDecimal.valueOf(reminders.getTotal() != null ? reminders.getTotal() : 0))
                .message(message)
                .build();
    }

    private MetricCard buildMetricCard(BigDecimal current, BigDecimal previous) {
        BigDecimal curr = Optional.ofNullable(current).orElse(BigDecimal.ZERO);
        BigDecimal prev = Optional.ofNullable(previous).orElse(BigDecimal.ZERO);

        int comparison = curr.compareTo(prev);
        Trend trend = (comparison > 0) ? Trend.UP :
                (comparison < 0) ? Trend.DOWN :
                        Trend.FLAT;

        Integer pctChange = calculatePercentageChange(curr, prev);

        return MetricCard.builder()
                .current(curr)
                .previous(prev)
                .percentageChange(pctChange)
                .trend(trend)
                .message(String.format("Trend is %s with a %d%% change", trend.getValue(), pctChange))
                .build();
    }

    private Integer calculatePercentageChange(BigDecimal current, BigDecimal previous) {
        if (previous.compareTo(BigDecimal.ZERO) == 0) {
            return current.compareTo(BigDecimal.ZERO) > 0 ? 100 : 0;
        }
        return current.subtract(previous)
                .divide(previous, 2, RoundingMode.HALF_EVEN)
                .multiply(BigDecimal.valueOf(100))
                .intValue();
    }
}