package com.app.budgets.dashboard.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DashboardRequestDTO {

    @Enumerated(EnumType.STRING)
    @NotNull
    private DashboardFilter filter;

}