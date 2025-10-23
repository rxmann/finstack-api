package com.app.budgets.paymentreminder.dto;

import com.app.budgets.common.enums.BudgetFrequency;
import com.app.budgets.common.enums.ReminderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentReminderResponse {

    private String id;
    private String categoryId;
    private String categoryName;
    private String reminderName;
    private BudgetFrequency frequency;
    private LocalDate nextDueDate;
    private ReminderStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long daysUntilDue;
    private Boolean shouldNotifyToday;
}