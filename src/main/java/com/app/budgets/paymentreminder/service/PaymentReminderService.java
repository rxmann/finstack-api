package com.app.budgets.paymentreminder.service;

import com.app.budgets.budget.dto.BudgetRequest;
import com.app.budgets.budget.repository.BudgetCategoryRepository;
import com.app.budgets.budget.service.BudgetService;
import com.app.budgets.common.enums.ReminderStatus;
import com.app.budgets.paymentreminder.dto.AcknowledgeReminderRequest;
import com.app.budgets.paymentreminder.dto.PaymentReminderRequest;
import com.app.budgets.paymentreminder.dto.PaymentReminderResponse;
import com.app.budgets.paymentreminder.dto.UpdateReminderStatusRequest;
import com.app.budgets.paymentreminder.mapper.PaymentReminderMapper;
import com.app.budgets.paymentreminder.repository.PaymentReminderRepository;
import com.app.budgets.user.UserAuth;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentReminderService {

    private final PaymentReminderRepository paymentReminderRepository;
    private final PaymentReminderMapper paymentReminderMapper;
    private final BudgetCategoryRepository budgetCategoryRepository;
    private final UserAuth userAuth;
    private final BudgetService budgetService;

    @Transactional(readOnly = true)
    public List<PaymentReminderResponse> getAllPaymentReminders() throws Exception {
        var currentUser = userAuth.getCurrentUser();

        return paymentReminderRepository.findByUserIdOrderByNextDueDateAsc(currentUser.getId())
                .stream()
                .map(paymentReminderMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PaymentReminderResponse> getUpcomingPaymentReminders() throws Exception {
        var currentUser = userAuth.getCurrentUser();

        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusDays(30); // Next 30 days

        return paymentReminderRepository.findUpcomingReminders(currentUser, today, endDate)
                .stream()
                .map(paymentReminderMapper::toResponse)
                .toList();
    }

    @Transactional
    public PaymentReminderResponse createPaymentReminder(PaymentReminderRequest request) throws Exception {
        var currentUser = userAuth.getCurrentUser();

        var reminder = paymentReminderMapper.toEntity(request);
        reminder.setUser(currentUser);

        if (request.getCategoryId() != null) {
            var category = budgetCategoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new Exception("Budget category not found with id: " + request.getCategoryId()));
            reminder.setBudgetCategory(category);
        }

        var saved = paymentReminderRepository.save(reminder);

        log.info("Created payment reminder: {} for user: {}", saved.getId(), currentUser.getId());
        return paymentReminderMapper.toResponse(saved);
    }

    @Transactional
    public PaymentReminderResponse updatePaymentReminder(PaymentReminderRequest request) throws Exception {
        var currentUser = userAuth.getCurrentUser();

        var reminder = paymentReminderRepository.findById(request.getId())
                .orElseThrow(() -> new Exception("Payment reminder not found with id: " + request.getId()));

        if (!reminder.getUser().getId().equals(currentUser.getId())) {
            throw new Exception("You don't have permission to update this payment reminder");
        }

        paymentReminderMapper.updateEntity(request, reminder);

        if (request.getCategoryId() != null) {
            var category = budgetCategoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new Exception("Budget category not found with id: " + request.getCategoryId()));
            reminder.setBudgetCategory(category);
        } else {
            reminder.setBudgetCategory(null);
        }

        var updated = paymentReminderRepository.save(reminder);

        log.info("Updated payment reminder: {}", updated.getId());
        return paymentReminderMapper.toResponse(updated);
    }

    @Transactional
    public PaymentReminderResponse snoozeReminder(String id) throws Exception {
        var currentUser = userAuth.getCurrentUser();

        var reminder = paymentReminderRepository.findById(id)
                .orElseThrow(() -> new Exception("Payment reminder not found with id: " + id));

        if (!reminder.getUser().getId().equals(currentUser.getId())) {
            throw new Exception("You don't have permission to snooze this payment reminder");
        }

        if (reminder.getStatus().equals(ReminderStatus.SNOOZED)) {
            reminder.setStatus(ReminderStatus.ACTIVE);
        }
        else {
            reminder.setStatus(ReminderStatus.SNOOZED);
        }


        reminder.setNextDueDate(reminder.getNextDueDate().plusDays(1)); // Snooze for 1 day

        var updated = paymentReminderRepository.save(reminder);

        log.info("Snoozed reminder: {}", id);
        return paymentReminderMapper.toResponse(updated);
    }

    @Transactional
    public PaymentReminderResponse acknowledgeReminder(String id, AcknowledgeReminderRequest request) throws Exception {
        var currentUser = userAuth.getCurrentUser();

        var reminder = paymentReminderRepository.findById(id)
                .orElseThrow(() -> new Exception("Payment reminder not found"));

        if (!reminder.getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Unauthorized");
        }

        // CREATE BUDGET ENTRY
        var budgetRequest = BudgetRequest.builder()
                .amount(request.getAmount())
                .name(request.getName())
                .budgetDate(request.getBudgetDate())
                .receiptUrl(request.getReceiptUrl())
                .tags(request.getTags())
                .budgetCategoryId(reminder.getBudgetCategory().getId())
                .build();

        budgetService.createBudget(budgetRequest);

        // Calculate next due date
        calculateNextDueDate(reminder);
        reminder.setStatus(ReminderStatus.ACTIVE);

        var updated = paymentReminderRepository.save(reminder);
        return paymentReminderMapper.toResponse(updated);
    }

    @Transactional
    public void deletePaymentReminder(String id) throws Exception {
        var currentUser = userAuth.getCurrentUser();

        var reminder = paymentReminderRepository.findById(id)
                .orElseThrow(() -> new Exception("Payment reminder not found with id: " + id));

        if (!reminder.getUser().getId().equals(currentUser.getId())) {
            throw new Exception("You don't have permission to delete this payment reminder");
        }

        paymentReminderRepository.delete(reminder);
        log.info("Deleted payment reminder: {}", id);
    }

    @Transactional(readOnly = true)
    public List<PaymentReminderResponse> getRemindersToNotify() throws Exception {
        var currentUser = userAuth.getCurrentUser();

        LocalDate today = LocalDate.now();
        LocalDate oneDayFromNow = today.plusDays(1);
        LocalDate threeDaysFromNow = today.plusDays(3);

        return paymentReminderRepository.findRemindersToNotify(currentUser, today, oneDayFromNow, threeDaysFromNow)
                .stream()
                .map(paymentReminderMapper::toResponse)
                .toList();
    }

    private void calculateNextDueDate(com.app.budgets.paymentreminder.model.PaymentReminder reminder) {
        LocalDate currentDueDate = reminder.getNextDueDate();
        LocalDate nextDueDate;

        switch (reminder.getFrequency()) {
            case MONTHLY -> nextDueDate = currentDueDate.plusMonths(1);
            case QUARTERLY -> nextDueDate = currentDueDate.plusMonths(3);
            case YEARLY -> nextDueDate = currentDueDate.plusYears(1);
            case ONE_TIME -> {
                // For one-time reminders, mark as completed
                reminder.setStatus(ReminderStatus.COMPLETED);
                return;
            }
            default -> throw new IllegalStateException("Unexpected frequency: " + reminder.getFrequency());
        }

        reminder.setNextDueDate(nextDueDate);
    }
}