package com.app.budgets.budget.repository;

import com.app.budgets.budget.model.BudgetType;
import com.app.budgets.dashboard.dto.response.CashFlowResponse;
import com.app.budgets.dashboard.dto.Granularity;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
public class BudgetRepositoryImpl implements CashFlowRepositoryCustom {

    private final EntityManager em;

    @Override
    public List<CashFlowResponse> getCashFlowData(
            String userId,
            Granularity granularity,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Set<BudgetType> incomeTypes,
            Set<BudgetType> expenseTypes
    ) {

        String sql = """
                WITH date_filler AS (
                    SELECT generate_series(
                        date_trunc('%s', CAST(:startDate AS timestamp)),
                        date_trunc('%s', CAST(:endDate AS timestamp) - interval '1 day'),
                        interval '%s'
                    ) AS period
                ),
                base_data AS (
                    SELECT 
                        date_trunc('%s', b.budget_date) AS period,
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
                    to_char(df.period, '%s') AS dateRange, 
                    df.period::date AS period,
                    COALESCE(bd.income_amount, 0) AS incomeAmount,
                    COALESCE(bd.expense_amount, 0) AS expenseAmount
                FROM date_filler df
                LEFT JOIN base_data bd ON bd.period = df.period
                ORDER BY df.period
                """
                .formatted(
                        granularity.getTruncUnit(),
                        granularity.getTruncUnit(),
                        granularity.getInterval(),
                        granularity.getTruncUnit(),
                        granularity.getLabelFormat()
                );

        return em.createNativeQuery(sql, "CashFlowMapping")
                .setParameter("userId", userId)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getResultList();
    }
}