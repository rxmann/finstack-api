package com.app.budgets.dashboard;

import com.app.budgets.budget.model.BudgetType;
import com.app.budgets.budget.repository.BudgetRepository;
import com.app.budgets.common.enums.DateRange;
import com.app.budgets.dashboard.dto.Granularity;
import com.app.budgets.dashboard.dto.Trend;
import com.app.budgets.dashboard.dto.metric.*;
import com.app.budgets.dashboard.dto.request.DashboardRequest;
import com.app.budgets.dashboard.dto.response.*;
import com.app.budgets.dashboard.dto.treemap.TreeMapResponse;
import com.app.budgets.dashboard.dto.treemap.TreeNode;
import com.app.budgets.dashboard.services.GranularityResolver;
import com.app.budgets.paymentreminder.repository.PaymentReminderRepository;
import com.app.budgets.user.UserAuth;
import com.app.budgets.util.FilterUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.app.budgets.budget.model.BudgetType.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class DashboardAnalyticsService {
    private static final Set<BudgetType> INCOME_TYPES = Set.of(INCOME, SAVINGS, INVESTMENT, LOAN);
    private static final Set<BudgetType> EXPENSE_TYPES = Set.of(EXPENSE, LEND, EXTRA);
    private final BudgetRepository budgetRepository;
    private final PaymentReminderRepository paymentReminderRepository;
    private final UserAuth userAuth;

    public Optional<DashboardResponse> getDashboardAnalytics(DashboardRequest requestDTO) {
        var user = userAuth.getCurrentUser();
        log.info("Getting dashboard MetricsAnalytics for filter {}", requestDTO.toString());

        DateRange dateRange = FilterUtil.calculateDates(requestDTO.getFilter());

        BudgetSummary summary = budgetRepository.sumBudgetByUser(user.getId(), dateRange.startDate(), dateRange.endDate(), dateRange.prevStartDate(), dateRange.prevEndDate(), INCOME_TYPES, EXPENSE_TYPES);

        RecurringMetrics recurring = budgetRepository.getGeneratedRecurringBudget(user.getId(), dateRange.startDate(), dateRange.endDate());
        ReminderMetrics reminders = paymentReminderRepository.getReminderMetrics(user.getId());

        MetricCard incomeCard = buildMetricCard(summary.getCurrentIncome(), summary.getPreviousIncome());
        MetricCard expenseCard = buildMetricCard(summary.getCurrentExpense(), summary.getPreviousExpense());
        RecurringMetricCard recurringCard = buildRecurringCard(recurring);
        ReminderMetricCard reminderCard = buildReminderCard(reminders);

        BigDecimal net = BigDecimal.ZERO;
        if (summary.getCurrentIncome() != null) net = net.add(summary.getCurrentIncome());
        if (summary.getCurrentExpense() != null) net = net.subtract(summary.getCurrentExpense());


        return Optional.of(DashboardResponse.builder().income(incomeCard).expense(expenseCard).recurring(recurringCard).reminders(reminderCard).net(net.intValue()).build());
    }


    public List<CashFlowResponse> getCashFlowAnalytics(@Valid DashboardRequest requestDTO) {
        var user = userAuth.getCurrentUser();
        log.info("Getting dashboard CashFlowAnalytics for filter {}", requestDTO.toString());
        DateRange dateRange = FilterUtil.calculateDates(requestDTO.getFilter());
        Granularity granularity = GranularityResolver.resolveGranularity(requestDTO.getFilter());
        return budgetRepository.getCashFlowData(user.getId(), granularity, dateRange.startDate(), dateRange.endDate(), INCOME_TYPES, EXPENSE_TYPES);
    }

    public TreeMapResponse getBudgetComposition(DashboardRequest requestDTO) {
        var user = userAuth.getCurrentUser();
        DateRange dateRange = FilterUtil.calculateDates(requestDTO.getFilter());
        var rows = budgetRepository.getBudgetCompositionAnalytics(user.getId(), dateRange.startDate(), dateRange.endDate());
        return buildTreeMap(rows);
    }

    public List<ExpenseDistributionMetric> getExpenseDistribution(DashboardRequest requestDTO) {
        var user = userAuth.getCurrentUser();
        DateRange dateRange = FilterUtil.calculateDates(requestDTO.getFilter());
        Granularity granularity = GranularityResolver.resolveGranularity(requestDTO.getFilter());

        return budgetRepository.getExpenseDistribution(user.getId(), granularity, dateRange.startDate(), dateRange.endDate());
    }

    /**
     * HELPERS
     */
    private TreeMapResponse buildTreeMap(List<BudgetComposition> rows) {

        Map<String, List<TreeNode>> grouped = groupByType(rows);
        TreeNode incomeNode = TreeNode.builder().name("Income").children(grouped.getOrDefault("INCOME", List.of())).build();
        TreeNode expenseNode = TreeNode.builder().name("Expense").children(grouped.getOrDefault("EXPENSE", List.of())).build();

        return TreeMapResponse.builder().name("Budget Composition").children(List.of(incomeNode, expenseNode)).build();
    }

    private Map<String, List<TreeNode>> groupByType(List<BudgetComposition> rows) {

        Map<String, List<TreeNode>> map = new HashMap<>();

        for (var r : rows) {
            String parentKey = INCOME_TYPES.contains(r.getBudgetType()) ? "INCOME" : "EXPENSE";
            TreeNode node = TreeNode.builder().name(r.getCategory()).value(r.getAmount()).percentage(r.getAmountPct()).build();
            map.computeIfAbsent(parentKey, k -> new ArrayList<>()).add(node);
        }
        return map;
    }


    private RecurringMetricCard buildRecurringCard(RecurringMetrics recurring) {
        BigDecimal amount = Optional.ofNullable(recurring.getSum()).orElse(BigDecimal.ZERO);

        return RecurringMetricCard.builder().totalSum(amount).totalCount(recurring.getCount()).message(String.format("%d payments due this period", recurring.getCount())).build();
    }

    private ReminderMetricCard buildReminderCard(ReminderMetrics reminder) {
        return ReminderMetricCard.builder().overdueCount(reminder.getOverdue()).dueSoonCount(reminder.getDueSoon()).totalReminders(reminder.getTotal()).nextDueDate(reminder.getNextDueDate()).message(generateStatusMessage(reminder)).build();
    }

    private String generateStatusMessage(ReminderMetrics reminder) {
        int overdue = reminder.getOverdue();
        int dueSoon = reminder.getDueSoon();
        if (overdue > 0) {
            return String.format("%d item%s overdue", overdue, overdue > 1 ? "s" : "");
        }
        if (dueSoon > 0) {
            String date = reminder.getNextDueDate() != null ? reminder.getNextDueDate().format(DateTimeFormatter.ofPattern("MMM d")) : "soon";
            return String.format("%d item%s due %s", dueSoon, dueSoon > 1 ? "s" : "", date);
        }
        return "All on track";
    }

    private MetricCard buildMetricCard(BigDecimal current, BigDecimal previous) {
        BigDecimal curr = Optional.ofNullable(current).orElse(BigDecimal.ZERO);
        BigDecimal prev = Optional.ofNullable(previous).orElse(BigDecimal.ZERO);

        int comparison = curr.compareTo(prev);
        Trend trend = (comparison > 0) ? Trend.UP : (comparison < 0) ? Trend.DOWN : Trend.FLAT;

        Integer pctChange = calculatePercentageChange(curr, prev);

        return MetricCard.builder().current(curr).previous(prev).percentageChange(pctChange).trend(trend).message(String.format("Trend is %s with a %d%% change", trend.getValue(), pctChange)).build();
    }

    private Integer calculatePercentageChange(BigDecimal current, BigDecimal previous) {
        if (previous.compareTo(BigDecimal.ZERO) == 0) {
            return current.compareTo(BigDecimal.ZERO) > 0 ? 100 : 0;
        }
        return current.subtract(previous).divide(previous, 2, RoundingMode.HALF_EVEN).multiply(BigDecimal.valueOf(100)).intValue();
    }

}