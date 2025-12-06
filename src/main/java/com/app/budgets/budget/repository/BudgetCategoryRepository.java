package com.app.budgets.budget.repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.budgets.budget.model.BudgetCategory;

@Repository
public interface BudgetCategoryRepository extends JpaRepository<BudgetCategory, String> {
    List<BudgetCategory> findAllByUserId(String userId);

    boolean existsByUserIdAndName(String userId, String name);

    Page<BudgetCategory> findAllByUserIdAndIsActiveTrue(String id, Pageable pageable);

    Optional<BudgetCategory> findByIdAndUserIdAndIsActiveTrue(String categoryId, String userId);

    boolean existsByUserIdAndNameAndIdNot(String userId, @NotBlank String name, String categoryId);
}
