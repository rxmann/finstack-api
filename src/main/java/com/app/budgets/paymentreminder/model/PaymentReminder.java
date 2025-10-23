package com.app.budgets.paymentreminder.model;

import com.app.budgets.budget.model.BudgetCategory;
import com.app.budgets.common.enums.BudgetFrequency;
import com.app.budgets.common.enums.ReminderStatus;
import com.app.budgets.common.model.BaseEntity;
import com.app.budgets.user.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
@Table(name = "payment_reminders", indexes = {
        @Index(name = "idx_user_next_due_date", columnList = "user_id, next_due_date"),
        @Index(name = "idx_user_id", columnList = "user_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class PaymentReminder extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    @JsonIgnore
    private BudgetCategory budgetCategory;

    @Column(name = "reminder_name", nullable = false)
    private String reminderName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BudgetFrequency frequency;

    @Column(name = "next_due_date", nullable = false)
    private LocalDate nextDueDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ReminderStatus status = ReminderStatus.ACTIVE;
}
