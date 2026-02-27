package com.app.budgets.paymentreminder.repository;


import com.app.budgets.common.enums.ReminderStatus;
import com.app.budgets.dashboard.dto.ReminderMetrics;
import com.app.budgets.paymentreminder.model.PaymentReminder;
import com.app.budgets.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PaymentReminderRepository extends JpaRepository<PaymentReminder, String> {

    List<PaymentReminder> findByUserIdOrderByNextDueDateAsc(String userId);

    List<PaymentReminder> findByUserIdAndStatusOrderByNextDueDateAsc(String userId, ReminderStatus status);

    @Query("SELECT pr FROM PaymentReminder pr WHERE pr.user = :user " +
            "AND pr.nextDueDate >= :startDate AND pr.nextDueDate <= :endDate " +
            "AND pr.status = 'ACTIVE' " +
            "ORDER BY pr.nextDueDate ASC")
    List<PaymentReminder> findUpcomingReminders(
            @Param("user") User user,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("SELECT pr FROM PaymentReminder pr WHERE pr.status = 'ACTIVE' " +
            "AND (pr.nextDueDate = :threeDaysFromNow OR pr.nextDueDate = :oneDayFromNow OR pr.nextDueDate = :today)")
    List<PaymentReminder> findRemindersToNotify(
            @Param("today") LocalDate today,
            @Param("oneDayFromNow") LocalDate oneDayFromNow,
            @Param("threeDaysFromNow") LocalDate threeDaysFromNow
    );


    @Query("""
          SELECT
                SUM(case when pr.nextDueDate < CURRENT DATE and pr.status = 'ACTIVE' then 1 else 0 END) as overdue,
                SUM(case when pr.nextDueDate between current date   and current date  + 7 day AND pr.status = 'ACTIVE' THEN 1 ELSE 0 END) as dueSoon,
                SUM(case when pr.status = 'ACTIVE' then 1 else 0 end) as total,
                MIN(pr.nextDueDate) as nextDueDate
          FROM PaymentReminder pr
          where pr.user.id = :userId
          """)
    ReminderMetrics getReminderMetrics(String userId);
}