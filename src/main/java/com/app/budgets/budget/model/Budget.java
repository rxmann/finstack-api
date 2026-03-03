package com.app.budgets.budget.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.app.budgets.common.model.BaseEntity;
import com.app.budgets.dashboard.dto.response.CashFlowResponse;
import com.app.budgets.user.model.User;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;



@SqlResultSetMapping(
        name = "CashFlowMapping",
        classes = @ConstructorResult(
                targetClass = CashFlowResponse.class,
                columns = {
                        @ColumnResult(name = "dateRange", type = String.class),
                        @ColumnResult(name = "period", type = LocalDate.class),
                        @ColumnResult(name = "incomeAmount", type = BigDecimal.class),
                        @ColumnResult(name = "expenseAmount", type = BigDecimal.class)
                }
        )
)
@Entity
@Table(name = "budgets", indexes = {
        @Index(name = "idx_user_budget_date", columnList = "user_id, budget_date")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ToString(exclude = { "user" })
public class Budget extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "budget_category_id", nullable = false)
    private BudgetCategory budgetCategory;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(name = "budget_date", nullable = false)
    private LocalDateTime budgetDate;

    private String receiptUrl;

    private List<String> tags;

    @Builder.Default
    @Column(name = "is_recurring")
    private Boolean isRecurring = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recurring_budget_id", nullable = true)
    private RecurringBudget recurringBudget;
}
