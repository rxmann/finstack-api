package com.app.budgets.dashboard.dto.response;

import lombok.Builder;

@Builder
public record DashboardResponse(
        MetricCard income,
        MetricCard expense,
        RecurringMetricCard recurring,
        ReminderMetricCard reminders,
        Integer net
) {}
