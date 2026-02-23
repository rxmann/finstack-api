package com.app.budgets.dashboard;

import com.app.budgets.budget.dto.BudgetSummary;
import com.app.budgets.budget.dto.RecurringMetrics;
import com.app.budgets.budget.model.BudgetType;
import com.app.budgets.budget.repository.BudgetRepository;
import com.app.budgets.budget.repository.RecurringBudgetRepository;
import com.app.budgets.common.enums.DateRange;
import com.app.budgets.dashboard.dto.DashboardRequestDTO;
import com.app.budgets.dashboard.dto.DashboardResponseDTO;
import com.app.budgets.dashboard.dto.MetricCard;
import com.app.budgets.dashboard.dto.Trend;
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
        log.info("Date Range result: {}", dateRange);
        BudgetSummary summary = budgetRepository.sumBudgetByUser(
                user.getId(),
                dateRange.startDate(),
                dateRange.endDate(),
                dateRange.prevStartDate(),
                dateRange.prevEndDate(),
                INCOME_TYPES,
                EXPENSE_TYPES
        );

        // TODO: recurring and reminder metrics
        RecurringMetrics recurring = budgetRepository.getRecurringMetrics(user.getId(), dateRange.startDate(), dateRange.endDate());
        MetricCard recurringMetricCard = MetricCard.builder().current(recurring.getCount()).net(recurring.getSum()).build();

        return Optional.of(DashboardResponseDTO.builder()
                .income(buildMetricCard(summary.getCurrentIncome(), summary.getPreviousIncome()))
                .expense(buildMetricCard(summary.getCurrentExpense(), summary.getPreviousExpense()))
                .recurring(recurringMetricCard)
                .build());
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