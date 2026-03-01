package com.app.budgets.budget.repository;

import com.app.budgets.budget.model.Budget;
import com.app.budgets.budget.model.BudgetType;
import com.app.budgets.dashboard.dto.BudgetSummary;
import com.app.budgets.dashboard.dto.RecurringMetrics;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, String>, CashFlowRepositoryCustom {
    Page<Budget> findAllByUserId(String userId, Pageable pageable);

    Optional<Budget> findByIdAndUserId(String budgetId, String id);

    @Query("""
                SELECT 
                    COALESCE(SUM(CASE 
                        WHEN b.budgetDate >= :currentStart AND b.budgetDate < :currentEnd 
                        AND b.budgetCategory.budgetType IN :incomeTypes 
                        THEN b.amount ELSE 0 END), 0) AS currentIncome,
                    COALESCE(SUM(CASE 
                        WHEN b.budgetDate >= :prevStart AND b.budgetDate < :prevEnd 
                        AND b.budgetCategory.budgetType IN :incomeTypes 
                        THEN b.amount ELSE 0 END), 0) AS previousIncome,
                    COALESCE(SUM(CASE 
                        WHEN b.budgetDate >= :currentStart AND b.budgetDate < :currentEnd 
                        AND b.budgetCategory.budgetType IN :expenseTypes 
                        THEN b.amount ELSE 0 END), 0) AS currentExpense,
                    COALESCE(SUM(CASE 
                        WHEN b.budgetDate >= :prevStart AND b.budgetDate < :prevEnd 
                        AND b.budgetCategory.budgetType IN :expenseTypes 
                        THEN b.amount ELSE 0 END), 0) AS previousExpense
                FROM Budget b
                WHERE b.user.id = :userId
            """)
    BudgetSummary sumBudgetByUser(
            @Param("userId") String userId,
            @Param("currentStart") LocalDateTime currentStart,
            @Param("currentEnd") LocalDateTime currentEnd,
            @Param("prevStart") LocalDateTime prevStart,
            @Param("prevEnd") LocalDateTime prevEnd,
            @Param("incomeTypes") Set<BudgetType> incomeTypes,
            @Param("expenseTypes") Set<BudgetType> expenseTypes
    );


    @Query("""
                SELECT 
                    COUNT(b) as count,
                    COALESCE(SUM(b.amount), 0) as sum
                FROM Budget b
                WHERE b.user.id = :userId
                  AND b.recurringBudget IS NOT NULL
                  AND b.budgetDate >= :currentStart
                  AND b.budgetDate < :currentEnd
            """)
    RecurringMetrics getGeneratedRecurringBudget(
            @Param("userId") String userId,
            @Param("currentStart") LocalDateTime currentStart,
            @Param("currentEnd") LocalDateTime currentEnd
    );

}
