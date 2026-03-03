package com.app.budgets.dashboard.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;


@Data
@Builder
@AllArgsConstructor
public class ReminderMetricCard {
    private Integer overdueCount;
    private Integer dueSoonCount;
    private Integer totalReminders;
    private LocalDate nextDueDate;
    private String message;
}