package com.app.budgets.dashboard.dto;

import java.time.LocalDate;

public interface ReminderMetrics {
    Long getOverdue();      // Overdue count
    Long getDueSoon();      // Due in next 7 days
    Long getTotal();        // Total active reminders
    LocalDate getNextDueDate();
}