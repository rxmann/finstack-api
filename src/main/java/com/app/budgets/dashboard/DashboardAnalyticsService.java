package com.app.budgets.dashboard;

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
import java.util.UUID;

import static com.app.budgets.budget.model.BudgetType.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class DashboardAnalyticsService {
    private final BudgetRepository budgetRepository;
    private final RecurringBudgetRepository recurringBudgetRepository;
    private final PaymentReminderRepository paymentReminderRepository;
    private final UserAuth userAuth;

    private static final Set<BudgetType> INCOME_TYPES =
            Set.of(INCOME, SAVINGS, INVESTMENT, LOAN);
    public Optional<DashboardResponseDTO> getDashboardAnalytics(DashboardRequestDTO requestDTO) {
        var user = userAuth.getCurrentUser();
        log.info("Getting dashboard analytics for filter {}", requestDTO.toString());
        var dateRange = FilterUtil.calculateDates(requestDTO.getFilter());
        log.info("Date Range result: {}", dateRange);
        var result = buildIncomeMetric(user.getId(), dateRange);
        log.info("Income metric result: {}", result);
        return Optional.ofNullable(DashboardResponseDTO.builder().income(result).build());
    }

    private MetricCard buildIncomeMetric(String userId, DateRange range) {

        BigDecimal current = budgetRepository
                .sumByUserAndDateRangeAndType(
                        userId,
                        range.startDate(),
                        range.endDate(),
                        INCOME_TYPES
                );

        BigDecimal previous = budgetRepository
                .sumByUserAndDateRangeAndType(
                        userId,
                        range.prevStartDate(),
                        range.prevEndDate(),
                        INCOME_TYPES
                );

        return buildMetricCard(current, previous);
    }

    private MetricCard buildMetricCard(BigDecimal current, BigDecimal previous) {
        // 1. Handle nulls gracefully
        BigDecimal curr = Optional.ofNullable(current).orElse(BigDecimal.ZERO);
        BigDecimal prev = Optional.ofNullable(previous).orElse(BigDecimal.ZERO);

        log.info("Processing MetricCard - Current: {}, Previous: {}", curr, prev);

        // 2. Determine Trend using compareTo
        // compareTo returns: -1 (less than), 0 (equal), 1 (greater than)
        int comparison = curr.compareTo(prev);
        Trend trend = (comparison > 0) ? Trend.UP : (comparison < 0) ? Trend.DOWN : Trend.FLAT;

        // 3. Calculate Percentage Change
        Integer pctChange = calculatePercentageChange(curr, prev);

        String message = String.format("Trend is %s with a %d%% change", trend, pctChange);

        return new MetricCard(curr, prev, trend, message, pctChange);
    }

    private Integer calculatePercentageChange(BigDecimal current, BigDecimal previous) {
        if (previous.compareTo(BigDecimal.ZERO) == 0) {
            return current.compareTo(BigDecimal.ZERO) > 0 ? 100 : 0;
        }

        return current.subtract(previous)
                .divide(previous, 2, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .intValue();
    }
}