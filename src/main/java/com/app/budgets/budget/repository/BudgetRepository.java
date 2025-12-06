package com.app.budgets.budget.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.budgets.budget.model.Budget;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, String> {
    Page<Budget> findAllByUserId(String userId, Pageable pageable);

    Optional<Budget> findByIdAndUserId(String budgetId, String id);
}
