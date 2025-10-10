package com.app.budgets.budget.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Locale.Category;

import com.app.budgets.user.model.BaseEntity;
import com.app.budgets.user.model.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "recurring_budgets")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class RecurringBudget extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "budget_type", nullable = false)
    private BudgetType budgetType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private String frequency;

    @Builder.Default
    @Column(name = "frequency_interval")
    private Integer frequencyInterval = 1;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    private LocalDate endDate;

    @Column(name = "next_occurrence", nullable = false)
    private LocalDate nextOccurrence;

    @Builder.Default
    @Column(name = "is_active")
    private Boolean isActive = true;
}
