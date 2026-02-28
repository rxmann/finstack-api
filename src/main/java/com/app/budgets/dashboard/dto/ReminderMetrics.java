package com.app.budgets.dashboard.dto;

import java.time.LocalDate;

public interface ReminderMetrics {
    Integer getOverdue();      // Overdue count
    Integer getDueSoon();      // Due in next 7 days
    Integer getTotal();        // Total active reminders
    LocalDate getNextDueDate();
}