package com.app.budgets.budget.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.budgets.budget.model.BudgetCategory;

@Repository
public interface BudgetCategoryRepository extends JpaRepository<BudgetCategory, String> {
    List<BudgetCategory> findAllByUserId(String userId);
}
