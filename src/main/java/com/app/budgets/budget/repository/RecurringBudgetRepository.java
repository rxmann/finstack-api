package com.app.budgets.budget.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.app.budgets.budget.model.RecurringBudget;

@Repository
public interface RecurringBudgetRepository extends JpaRepository<RecurringBudget, String> {
    Page<RecurringBudget> findAllByUserId(String userId, Pageable pageable);

    @Query("""
        SELECT r FROM RecurringBudget r
        WHERE r.isActive = true 
          AND r.nextOccurrence <= :today
    """)
    List<RecurringBudget> findDueRecurringBudgets(@Param("today") LocalDate today);

}
