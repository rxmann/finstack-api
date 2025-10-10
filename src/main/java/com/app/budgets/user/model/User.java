package com.app.budgets.user.model;

import java.util.List;

import com.app.budgets.auth.model.AuthProviderType;
import com.app.budgets.budget.model.Budget;
import com.app.budgets.budget.model.BudgetCategory;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "users", indexes = {
        @Index(name = "idx_provider_id_provider_type", columnList = "provider_id, provider_type")
})
public class User extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(name = "password_hash", nullable = true)
    private String passwordHash;

    @Column(name = "is_active", nullable = false)
    private final Boolean isActive = true;

    @Column(name = "account_locked", nullable = true)
    private boolean accountLocked;

    @Column(name = "roles")
    private List<String> roles;

    @Column(name = "provider_id")
    private String providerId;

    @Column(name = "provider_type")
    @Enumerated(EnumType.STRING)
    private AuthProviderType authProviderType;

    // relations
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BudgetCategory> budgetCategories;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Budget> budgets;
}
