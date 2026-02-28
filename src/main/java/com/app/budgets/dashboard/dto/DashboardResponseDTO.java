package com.app.budgets.dashboard.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardResponseDTO {
    private MetricCard income;
    private MetricCard expense;
    private RecurringMetricCard recurring;
    private ReminderMetricCard reminders;
    private Integer net;
}
