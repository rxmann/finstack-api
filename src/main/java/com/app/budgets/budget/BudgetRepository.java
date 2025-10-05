package com.app.budgets.budget;

import com.app.budgets.budget.model.Budget;
import com.app.budgets.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {
    List<Budget> findAllByUser(User user);
}
