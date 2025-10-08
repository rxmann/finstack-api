package com.app.budgets.budget.service;

import com.app.budgets.budget.dto.BudgetCategoryRequest;
import com.app.budgets.budget.dto.BudgetCategoryResponse;
import com.app.budgets.budget.mapper.BudgetCategoryMapper;
import com.app.budgets.budget.repository.BudgetCategoryRepository;
import com.app.budgets.user.UserAuth;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
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
