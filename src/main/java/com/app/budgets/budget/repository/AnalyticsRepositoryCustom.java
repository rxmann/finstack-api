package com.app.budgets.budget.repository;

import com.app.budgets.budget.model.BudgetType;
import com.app.budgets.dashboard.dto.Granularity;
import com.app.budgets.dashboard.dto.metric.ExpenseDistributionMetric;
import com.app.budgets.dashboard.dto.response.CashFlowResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface AnalyticsRepositoryCustom {
    List<CashFlowResponse> getCashFlowData(
            String userId,
            Granularity granularity,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Set<BudgetType> incomeTypes,
            Set<BudgetType> expenseTypes
    );

    List<ExpenseDistributionMetric> getExpenseDistribution(
            String userId,
            Granularity granularity,
            LocalDateTime startDate,
            LocalDateTime endDate
    );
}