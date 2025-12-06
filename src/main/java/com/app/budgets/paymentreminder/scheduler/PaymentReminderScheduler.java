package com.app.budgets.paymentreminder.scheduler;

import com.app.budgets.paymentreminder.model.PaymentReminder;
import com.app.budgets.paymentreminder.service.PaymentReminderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class PaymentReminderScheduler {

    private final PaymentReminderService paymentReminderService;

    public PaymentReminderScheduler(PaymentReminderService paymentReminderService) {
        this.paymentReminderService = paymentReminderService;
    }

    // sec min hour day month day-of-week
    @Scheduled(cron = "0 0 8,9 * * *")
    @Transactional
    public void processPaymentReminder() throws Exception {


        List<PaymentReminder> paymentReminderList = paymentReminderService.getRemindersToNotify();
        paymentReminderList.forEach((pr) -> {
            log.info("Processing Payment Reminder for Payment Reminder ID: {}", pr.getId());
            log.info(pr.toString());
        });

        // generate db notifications and let the UI poll for the unread notifications
        // web push later

    }

}
