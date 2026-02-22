package com.app.budgets.budget.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.app.budgets.budget.model.BudgetType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.app.budgets.budget.model.Budget;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, String> {
    Page<Budget> findAllByUserId(String userId, Pageable pageable);

    Optional<Budget> findByIdAndUserId(String budgetId, String id);

    @Query("""
                SELECT COALESCE(SUM(b.amount), 0)
                FROM Budget b
                WHERE b.user.id = :userId
                  AND b.budgetDate BETWEEN :start AND :end
                  AND b.budgetCategory.budgetType IN :types
            """)
    BigDecimal sumByUserAndDateRangeAndType(
            String userId,
            LocalDateTime start,
            LocalDateTime end,
            Set<BudgetType> types
    );
}
