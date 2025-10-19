package com.app.budgets.budget.model;

import com.app.budgets.user.model.BaseEntity;
import com.app.budgets.user.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;

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
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "budget_category_id", nullable = false)
    @JsonIgnore
    private BudgetCategory budgetCategory;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    private String name;
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "budget_type", nullable = false)
    private BudgetType budgetType;

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
