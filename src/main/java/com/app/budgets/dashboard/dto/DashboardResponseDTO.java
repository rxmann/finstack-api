package com.app.budgets.dashboard.dto;

import lombok.Builder;

@Builder
public record DashboardResponseDTO(
        MetricCard income,
        MetricCard expense,
        RecurringMetricCard recurring,
        ReminderMetricCard reminders,
        Integer net
) {}
