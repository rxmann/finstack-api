package com.app.budgets.paymentreminder.mapper;


import com.app.budgets.common.enums.ReminderStatus;
import com.app.budgets.paymentreminder.dto.PaymentReminderRequest;
import com.app.budgets.paymentreminder.dto.PaymentReminderResponse;
import com.app.budgets.paymentreminder.model.PaymentReminder;
import org.mapstruct.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Mapper(componentModel = "spring")
public interface PaymentReminderMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "budgetCategory", ignore = true)
    @Mapping(target = "status", defaultValue = "ACTIVE")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "lastModifiedAt", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    PaymentReminder toEntity(PaymentReminderRequest request);

    @Mapping(target = "categoryId", source = "budgetCategory.id")
    @Mapping(target = "categoryName", source = "budgetCategory.name")
    @Mapping(target = "daysUntilDue", expression = "java(calculateDaysUntilDue(paymentReminder.getNextDueDate()))")
    @Mapping(target = "shouldNotifyToday", expression = "java(shouldNotifyToday(paymentReminder))")
    PaymentReminderResponse toResponse(PaymentReminder paymentReminder);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "budgetCategory", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "lastModifiedAt", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    void updateEntity(PaymentReminderRequest request, @MappingTarget PaymentReminder paymentReminder);

    default Long calculateDaysUntilDue(LocalDate nextDueDate) {
        return ChronoUnit.DAYS.between(LocalDate.now(), nextDueDate);
    }

    default Boolean shouldNotifyToday(PaymentReminder reminder) {
        if (reminder.getStatus() != ReminderStatus.ACTIVE) {
            return false;
        }

        long daysUntil = calculateDaysUntilDue(reminder.getNextDueDate());
        // Notify 3 days before, 1 day before, or on due date
        return daysUntil == 3 || daysUntil == 1 || daysUntil == 0;
    }
}
