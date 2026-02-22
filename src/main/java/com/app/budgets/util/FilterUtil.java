package com.app.budgets.util;

import com.app.budgets.common.enums.DateRange;
import com.app.budgets.dashboard.dto.DashboardFilter;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;

@Component
public class FilterUtil {

    public static DateRange calculateDates(DashboardFilter filter) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDate;
        LocalDateTime prevStartDate;
        LocalDateTime prevEndDate;

        switch (filter) {
            case THIS_WEEK -> {
                startDate = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY)).with(LocalTime.MIN);
                prevStartDate = startDate.minusWeeks(1);
                prevEndDate = startDate.minusNanos(1);
            }
            case THIS_MONTH -> {
                startDate = now.with(TemporalAdjusters.firstDayOfMonth()).with(LocalTime.MIN);
                prevStartDate = startDate.minusMonths(1);
                prevEndDate = startDate.minusNanos(1);
            }
            case THIS_QUARTER -> {
                int month = now.getMonthValue();
                int quarterStartMonth = ((month - 1) / 3) * 3 + 1;
                startDate = LocalDateTime.of(now.getYear(), quarterStartMonth, 1, 0, 0);
                prevStartDate = startDate.minusMonths(3);
                prevEndDate = startDate.minusNanos(1);
            }
            case THIS_YEAR -> {
                startDate = now.with(TemporalAdjusters.firstDayOfYear()).with(LocalTime.MIN);
                prevStartDate = startDate.minusYears(1);
                prevEndDate = startDate.minusNanos(1);
            }
            default -> throw new IllegalArgumentException("Invalid Filter");
        }

        return new DateRange(startDate, now, prevStartDate, prevEndDate);
    }
}
