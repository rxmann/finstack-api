package com.app.budgets.budget;

import com.app.budgets.budget.model.BudgetCategory;
import com.app.budgets.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BudgetCategoryRepository extends JpaRepository<BudgetCategory, Long> {
    List<BudgetCategory> findAllByUserOrUserIsNull(User user);
}
