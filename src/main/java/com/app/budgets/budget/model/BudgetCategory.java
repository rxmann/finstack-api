package com.app.budgets.budget.model;

import com.app.budgets.user.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "budget_categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BudgetCategory extends BaseEntity {

    @Column(name = "user_id")
    private String userId;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(nullable = false, length = 50)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "category_type", nullable = false, length = 20)
    private BudgetType categoryType;
}
