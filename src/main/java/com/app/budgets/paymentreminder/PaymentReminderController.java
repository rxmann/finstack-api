package com.app.budgets.paymentreminder;

import com.app.budgets.paymentreminder.dto.AcknowledgeReminderRequest;
import com.app.budgets.paymentreminder.dto.PaymentReminderRequest;
import com.app.budgets.paymentreminder.dto.PaymentReminderResponse;
import com.app.budgets.paymentreminder.dto.UpdateReminderStatusRequest;
import com.app.budgets.paymentreminder.dto.groups.ValidationGroupsPaymentReminder.CreatePaymentReminder;
import com.app.budgets.paymentreminder.dto.groups.ValidationGroupsPaymentReminder.UpdatePaymentReminder;
import com.app.budgets.paymentreminder.service.PaymentReminderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payment-reminders")
@RequiredArgsConstructor
public class PaymentReminderController {

    private final PaymentReminderService paymentReminderService;

    @GetMapping
    public ResponseEntity<List<PaymentReminderResponse>> getAllPaymentReminders() throws Exception {
        var response = paymentReminderService.getAllPaymentReminders();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<PaymentReminderResponse>> getUpcomingPaymentReminders() throws Exception {
        var response = paymentReminderService.getUpcomingPaymentReminders();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/notifications")
    public ResponseEntity<List<PaymentReminderResponse>> getRemindersToNotify() throws Exception {
        var response = paymentReminderService.getRemindersToNotify();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<PaymentReminderResponse> createPaymentReminder(
            @Validated(CreatePaymentReminder.class) @RequestBody PaymentReminderRequest request)
            throws Exception {
        var response = paymentReminderService.createPaymentReminder(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentReminderResponse> updatePaymentReminder(
            @PathVariable String id,
            @Validated(UpdatePaymentReminder.class) @RequestBody PaymentReminderRequest request)
            throws Exception {
        if (!id.equals(request.getId())) {
            throw new Exception("Path ID does not match request body ID");
        }
        var response = paymentReminderService.updatePaymentReminder(request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/snooze")
    public ResponseEntity<PaymentReminderResponse> snoozeReminder(@PathVariable String id) throws Exception {
        var response = paymentReminderService.snoozeReminder(id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/acknowledge")
    public ResponseEntity<PaymentReminderResponse> acknowledgeReminder(
            @PathVariable String id,
            @Validated @RequestBody AcknowledgeReminderRequest request) throws Exception {
        var response = paymentReminderService.acknowledgeReminder(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaymentReminder(@PathVariable String id) throws Exception {
        paymentReminderService.deletePaymentReminder(id);
        return ResponseEntity.noContent().build();
    }
}