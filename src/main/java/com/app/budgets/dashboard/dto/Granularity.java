package com.app.budgets.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Granularity {
    DAILY("1 day", "day", "DD Mon"),
    WEEKLY("1 week", "week", "\"W\"YYYY"),
    MONTHLY("1 month", "month", "Mon YYYY"),
    YEARLY("1 year", "year", "YYYY");

    private final String interval;
    private final String truncUnit;
    private final String labelFormat;
}