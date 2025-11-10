package com.app.budgets.budget.service;

import com.app.budgets.budget.dto.BudgetCategoryRequest;
import com.app.budgets.budget.dto.BudgetCategoryResponse;
import com.app.budgets.budget.mapper.BudgetCategoryMapper;
import com.app.budgets.budget.model.BudgetCategory;
import com.app.budgets.budget.repository.BudgetCategoryRepository;
import com.app.budgets.user.UserAuth;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BudgetCategoryService {

    private final BudgetCategoryRepository categoryRepository;
    private final BudgetCategoryMapper categoryMapper;
    private final UserAuth userAuth;

    @Transactional
    public BudgetCategoryResponse createCategory(BudgetCategoryRequest request) {
        var user = userAuth.getCurrentUser();
        // Check for duplicate category name per user
        if (categoryRepository.existsByUserIdAndName(user.getId(), request.getName())) {
            throw new IllegalArgumentException("Category with this name already exists");
        }

        var category = categoryMapper.toEntity(request);
        category.setUser(user);
        var saved = categoryRepository.save(category);
        return categoryMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<BudgetCategoryResponse> getAllCategories() {
        var user = userAuth.getCurrentUser();
        return categoryRepository.findAllByUserIdAndIsActiveTrue(user.getId())
                .stream()
                .map(categoryMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public BudgetCategoryResponse getCategoryById(String categoryId) {
        var user = userAuth.getCurrentUser();
        var category = categoryRepository.findByIdAndUserIdAndIsActiveTrue(categoryId, user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found or access denied"));
        return categoryMapper.toResponse(category);
    }

    @Transactional
    public BudgetCategoryResponse updateCategory(String categoryId, BudgetCategoryRequest request) {
        var user = userAuth.getCurrentUser();

        var category = categoryRepository.findByIdAndUserIdAndIsActiveTrue(categoryId, user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found or access denied"));

        // Check duplicate name (excluding current category)
        if (categoryRepository.existsByUserIdAndNameAndIdNot(user.getId(), request.getName(), category.getId())) {
            throw new IllegalArgumentException("Category with this name already exists");
        }

        categoryMapper.updateEntity(request, category);
        var updated = categoryRepository.save(category);
        return categoryMapper.toResponse(updated);
    }

    @Transactional
    public void deleteCategory(String categoryId) {
        var user = userAuth.getCurrentUser();
        var category = categoryRepository.findByIdAndUserIdAndIsActiveTrue(categoryId, user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found or access denied"));

        // Soft delete
        category.setIsActive(false);
        categoryRepository.save(category);
    }
}
