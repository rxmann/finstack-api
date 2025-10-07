package com.app.budgets.budget.service;

import java.util.List;

import com.app.budgets.user.UserAuth;
import org.springframework.stereotype.Service;

import com.app.budgets.budget.repository.BudgetCategoryRepository;
import com.app.budgets.budget.dto.BudgetCategoryRequest;
import com.app.budgets.budget.dto.BudgetCategoryResponse;
import com.app.budgets.budget.mapper.BudgetCategoryMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BudgetCategoryService {

    private final BudgetCategoryRepository categoryRepository;
    private final BudgetCategoryMapper categoryMapper;
    private final UserAuth userAuth;

    public BudgetCategoryResponse createCategory(BudgetCategoryRequest request) {
        var user = userAuth.getCurrentUser();
        var category = categoryMapper.toEntity(request);
        category.setUser(user);
        var saved = categoryRepository.save(category);
        return categoryMapper.toResponse(saved);
    }

    public List<BudgetCategoryResponse> getAllCategories() {
        var user = userAuth.getCurrentUser();
        return categoryRepository.findAllByUserId(user.getId()).stream()
                .map(categoryMapper::toResponse)
                .toList();
    }
}
