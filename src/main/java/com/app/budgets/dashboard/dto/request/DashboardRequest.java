package com.app.budgets.dashboard.dto.request;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DashboardRequest {

    @Enumerated(EnumType.STRING)
    @NotNull
    private DashboardFilter filter;

}