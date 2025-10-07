package com.app.budgets.budget.model;

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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "budgets")
public class Budget extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 20)
    private BudgetType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private BudgetCategory category;

    @Column(nullable = false)
    private Double amount;

    @Column(length = 255)
    private String note;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
