package com.app.budgets.budget.service;

import com.app.budgets.budget.BudgetCategoryRepository;
import com.app.budgets.budget.dto.BudgetCategoryRequest;
import com.app.budgets.budget.dto.BudgetCategoryResponse;
import com.app.budgets.budget.mapper.BudgetCategoryMapper;
import com.app.budgets.user.model.UserAuth;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BudgetCategoryService {

    private final BudgetCategoryRepository categoryRepository;
    private final BudgetCategoryMapper categoryMapper;
    private final UserAuth userAuth;

    public BudgetCategoryResponse createCategory(BudgetCategoryRequest request) {
        var user = userAuth.getCurrentUser();
        var category = categoryMapper.toEntity(request);
        category.setUserId(user.getId());
        var saved = categoryRepository.save(category);
        return categoryMapper.toResponse(saved);
    }

    public List<BudgetCategoryResponse> getAllCategories() {
        var user = userAuth.getCurrentUser();
        return categoryRepository.findAllByUserOrUserIsNull(user).stream().map(categoryMapper::toResponse).toList();
    }
}
