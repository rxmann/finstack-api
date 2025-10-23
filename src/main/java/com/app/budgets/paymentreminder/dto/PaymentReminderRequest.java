package com.app.budgets.paymentreminder.dto;

import com.app.budgets.common.enums.BudgetFrequency;
import com.app.budgets.common.enums.ReminderStatus;
import com.app.budgets.paymentreminder.dto.groups.ValidationGroupsPaymentReminder.CreatePaymentReminder;
import com.app.budgets.paymentreminder.dto.groups.ValidationGroupsPaymentReminder.UpdatePaymentReminder;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentReminderRequest {

    @NotNull(groups = UpdatePaymentReminder.class, message = "ID is required for update")
    @Null(groups = CreatePaymentReminder.class, message = "ID must be null for create")
    private String id;

    @NotBlank(message = "Select a budget category.", groups = {CreatePaymentReminder.class, UpdatePaymentReminder.class})
    private String categoryId;

    @NotBlank(message = "Reminder name is required", groups = {CreatePaymentReminder.class, UpdatePaymentReminder.class})
    private String reminderName;

    @NotNull(message = "Frequency is required", groups = {CreatePaymentReminder.class, UpdatePaymentReminder.class})
    private BudgetFrequency frequency;

    @NotNull(message = "Next Due date is required", groups = {CreatePaymentReminder.class, UpdatePaymentReminder.class})
    @FutureOrPresent(message = "Next Due date must be today or in the future", groups = CreatePaymentReminder.class)
    private LocalDate nextDueDate;

    @Enumerated(EnumType.STRING)
    private ReminderStatus status = ReminderStatus.ACTIVE;
}