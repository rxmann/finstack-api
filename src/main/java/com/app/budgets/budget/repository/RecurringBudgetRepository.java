package com.app.budgets.budget.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.budgets.budget.model.RecurringBudget;

@Repository
public interface RecurringBudgetRepository extends JpaRepository<RecurringBudget, String> {
    List<RecurringBudget> findAllByUserId(String userId);

}
