package com.app.budgets.budget.repository;

import com.app.budgets.dashboard.dto.BudgetSummary;
import com.app.budgets.dashboard.dto.CashFlow;
import com.app.budgets.dashboard.dto.Granularity;
import com.app.budgets.dashboard.dto.RecurringMetrics;
import com.app.budgets.budget.model.Budget;
import com.app.budgets.budget.model.BudgetType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, String> {
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

    @Query(value = """
            WITH date_filler AS (
                SELECT generate_series(
                    date_trunc(:truncUnit, CAST(:startDate AS timestamp)),
                    date_trunc(:truncUnit, CAST(:endDate AS timestamp) - interval '1 day'),
                    CAST(:intervalStep AS interval)
                ) AS period
            ),
            base_data AS (
                SELECT 
                    date_trunc(:truncUnit, b.budget_date) AS period,
                    SUM(CASE WHEN bc.budget_type IN ('INCOME','SAVINGS','INVESTMENT','LOAN') THEN b.amount ELSE 0 END) AS income_amount,
                    SUM(CASE WHEN bc.budget_type IN ('EXPENSE','LEND','EXTRA') THEN b.amount ELSE 0 END) AS expense_amount
                FROM budgets b
                JOIN budget_categories bc ON bc.id = b.budget_category_id
                WHERE b.user_id = :userId 
                  AND b.budget_date >= :startDate 
                  AND b.budget_date < :endDate
                GROUP BY 1
            )
            SELECT 
                to_char(df.period, :labelFormat) AS dateRange, 
                df.period::date AS period,
                COALESCE(bd.income_amount, 0) AS incomeAmount,
                COALESCE(bd.expense_amount, 0) AS expenseAmount
            FROM date_filler df
            LEFT JOIN base_data bd ON bd.period = df.period
            ORDER BY df.period
            """, nativeQuery = true)
    List<CashFlow> getCashFlowData(
            @Param("userId") String userId,
            @Param("intervalStep") String intervalStep, // e.g., granularity.getInterval()
            @Param("truncUnit") String truncUnit,       // e.g., granularity.getTruncUnit()
            @Param("labelFormat") String labelFormat,   // e.g., granularity.getLabelFormat()
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}
