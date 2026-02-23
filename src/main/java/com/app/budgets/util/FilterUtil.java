package com.app.budgets.util;

import com.app.budgets.common.enums.DateRange;
import com.app.budgets.dashboard.dto.DashboardFilter;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;

@Component
public class FilterUtil {

    public static DateRange calculateDates(DashboardFilter filter) {
        LocalDate today = LocalDate.now();
        LocalDate currentStart;
        LocalDate prevStart;

        switch (filter) {
            case THIS_WEEK -> {
                currentStart = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
                prevStart = currentStart.minusWeeks(1);
            }
            case THIS_MONTH -> {
                currentStart = today.with(TemporalAdjusters.firstDayOfMonth());
                prevStart = currentStart.minusMonths(1);
            }

            case THIS_QUARTER -> {
                int month = today.getMonthValue();
                int quarterStartMonth = ((month - 1) / 3) * 3 + 1;
                currentStart = LocalDate.of(today.getYear(), quarterStartMonth, 1);
                prevStart = currentStart.minusMonths(3);
            }

            case THIS_YEAR -> {
                currentStart = today.with(TemporalAdjusters.firstDayOfYear());
                prevStart = currentStart.minusYears(1);
            }

            default -> throw new IllegalArgumentException("Invalid Filter");
        }

        return new DateRange(
                currentStart.atStartOfDay(),
                today.plusDays(1).atStartOfDay(),
                prevStart.atStartOfDay(),
                currentStart.atStartOfDay()
        );
    }
}
