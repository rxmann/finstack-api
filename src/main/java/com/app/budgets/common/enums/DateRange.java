package com.app.budgets.common.enums;

import java.time.LocalDateTime;

public record DateRange(
        LocalDateTime startDate,
        LocalDateTime endDate,
        LocalDateTime prevStartDate,
        LocalDateTime prevEndDate
) {}