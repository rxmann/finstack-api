package com.app.budgets.dashboard.services;

import com.app.budgets.dashboard.dto.request.DashboardFilter;
import com.app.budgets.dashboard.dto.Granularity;
import org.springframework.stereotype.Service;

@Service
public class GranularityResolver {


    public static Granularity resolveGranularity(DashboardFilter filter) {
        return switch (filter) {
            case THIS_WEEK, THIS_MONTH -> Granularity.DAILY;
            case THIS_QUARTER, THIS_YEAR -> Granularity.MONTHLY;
        };
    }

}
