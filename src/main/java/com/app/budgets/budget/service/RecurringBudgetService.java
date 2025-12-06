package com.app.budgets.budget.service;

import com.app.budgets.budget.dto.RecurringBudgetRequest;
import com.app.budgets.budget.dto.RecurringBudgetResponse;
import com.app.budgets.budget.mapper.RecurringBudgetMapper;
import com.app.budgets.budget.model.BudgetCategory;
import com.app.budgets.budget.repository.BudgetCategoryRepository;
import com.app.budgets.budget.repository.RecurringBudgetRepository;
import com.app.budgets.common.enums.BudgetFrequency;
import com.app.budgets.user.UserAuth;
import com.app.budgets.user.UserRepository;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;


@Service
@Transactional
public class RecurringBudgetService {

    private final RecurringBudgetRepository recurringBudgetRepository;
    private final UserRepository userRepository;
    private final BudgetCategoryRepository budgetCategoryRepository;
    private final RecurringBudgetMapper recurringBudgetMapper;
    private final UserAuth userAuth;

    public RecurringBudgetService(RecurringBudgetRepository recurringBudgetRepository, UserRepository userRepository, BudgetCategoryRepository budgetCategoryRepository, RecurringBudgetMapper recurringBudgetMapper, UserAuth userAuth) {
        this.recurringBudgetRepository = recurringBudgetRepository;
        this.userRepository = userRepository;
        this.budgetCategoryRepository = budgetCategoryRepository;
        this.recurringBudgetMapper = recurringBudgetMapper;
        this.userAuth = userAuth;
    }

    @Transactional(readOnly = true)
    public List<RecurringBudgetResponse> getRecurringBudgets(Pageable pageable) {

        var user = userAuth.getCurrentUser();

        return recurringBudgetRepository.findAllByUserId(user.getId(), pageable).stream().map(recurringBudgetMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public RecurringBudgetResponse getRecurringBudget(String id) throws Exception {
        var currentUser = userAuth.getCurrentUser();

        var recurringBudget = recurringBudgetRepository.findById(id).orElseThrow(() -> new Exception("Recurring budget not found with id: " + id));

        if (!recurringBudget.getUser().getId().equals(currentUser.getId())) {
            throw new Exception("You don't have permission to view this recurring budget");
        }

        return recurringBudgetMapper.toResponse(recurringBudget);
    }

    @Transactional
    public RecurringBudgetResponse createRecurringBudget(RecurringBudgetRequest request) throws Exception {
        var currentUser = userAuth.getCurrentUser();
        var user = userRepository.findById(currentUser.getId()).orElseThrow();

        var category = findAndValidateCategory(request.getBudgetCategoryId(), user.getId());

        var recurringBudget = recurringBudgetMapper.toEntity(request);
        recurringBudget.setUser(user);
        recurringBudget.setBudgetCategory(category);
        recurringBudget.setNextOccurrence(calculateNextOccurrence(request.getStartDate(), request.getFrequency()));

        var saved = recurringBudgetRepository.save(recurringBudget);
        return recurringBudgetMapper.toResponse(saved);
    }

    @Transactional
    public RecurringBudgetResponse updateRecurringBudget(RecurringBudgetRequest request) throws Exception {
        var currentUser = userAuth.getCurrentUser();

        var existingBudget = recurringBudgetRepository.findById(request.getId()).orElseThrow();

        // Security: verify ownership
        if (!existingBudget.getUser().getId().equals(currentUser.getId())) {
            throw new Exception("Cannot update another user's recurring budget");
        }

        var category = findAndValidateCategory(request.getBudgetCategoryId(), currentUser.getId());

        // Update entity
        recurringBudgetMapper.updateEntity(request, existingBudget);
        existingBudget.setBudgetCategory(category);
        existingBudget.setNextOccurrence(calculateNextOccurrence(request.getStartDate(), request.getFrequency()));

        var saved = recurringBudgetRepository.save(existingBudget);
        return recurringBudgetMapper.toResponse(saved);
    }

    @Transactional
    protected BudgetCategory findAndValidateCategory(String categoryId, String userId) throws Exception {
        var category = budgetCategoryRepository.findById(categoryId).orElseThrow();

        if (!category.getUser().getId().equals(userId)) {
            throw new Exception("Cannot use another user's category");
        }

        return category;
    }

    private LocalDate calculateNextOccurrence(LocalDate startDate, BudgetFrequency frequency) {
        LocalDate now = LocalDate.now();
        if (startDate.isAfter(now)) {
            return startDate;
        }

        return getLocalDate(now, frequency);
    }

    public static LocalDate getLocalDate(LocalDate fromDate, BudgetFrequency frequency) {
        return switch (frequency) {
            case DAILY -> fromDate.plusDays(1);
            case WEEKLY -> fromDate.plusWeeks(1);
            case MONTHLY -> fromDate.plusMonths(1);
            case YEARLY -> fromDate.plusYears(1);
            case QUARTERLY -> fromDate.plusMonths(3);
            case ONE_TIME -> null;
        };
    }


    @Transactional
    public RecurringBudgetResponse disableRecurringBudget(String id) throws Exception {
        var currentUser = userAuth.getCurrentUser();

        var recurringBudget = recurringBudgetRepository.findById(id).orElseThrow(() -> new Exception("Recurring budget not found with id: " + id));

        // Verify ownership
        if (!recurringBudget.getUser().getId().equals(currentUser.getId())) {
            throw new Exception("You don't have permission to disable this recurring budget");
        }

        // Check if already disabled
        if (!recurringBudget.getIsActive()) {
            throw new BadRequestException("Recurring budget is already disabled");
        }

        recurringBudget.setIsActive(false);
        var updated = recurringBudgetRepository.save(recurringBudget);

        return recurringBudgetMapper.toResponse(updated);
    }

    @Transactional
    public RecurringBudgetResponse enableRecurringBudget(String id) throws Exception {
        var currentUser = userAuth.getCurrentUser();

        var recurringBudget = recurringBudgetRepository.findById(id).orElseThrow(() -> new Exception("Recurring budget not found with id: " + id));

        // Verify ownership
        if (!recurringBudget.getUser().getId().equals(currentUser.getId())) {
            throw new Exception("You don't have permission to enable this recurring budget");
        }

        // Check if already enabled
        if (recurringBudget.getIsActive()) {
            throw new BadRequestException("Recurring budget is already enabled");
        }

        recurringBudget.setIsActive(true);
        var updated = recurringBudgetRepository.save(recurringBudget);

        return recurringBudgetMapper.toResponse(updated);
    }

    @Transactional
    public void deleteRecurringBudget(String id) throws Exception {
        var currentUser = userAuth.getCurrentUser();

        var recurringBudget = recurringBudgetRepository.findById(id).orElseThrow(() -> new Exception("Recurring budget not found with id: " + id));

        // Verify ownership
        if (!recurringBudget.getUser().getId().equals(currentUser.getId())) {
            throw new Exception("You don't have permission to delete this recurring budget");
        }

        recurringBudgetRepository.delete(recurringBudget);
    }


}
