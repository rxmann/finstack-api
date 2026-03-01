package com.app.budgets.budget.repository;

import com.app.budgets.dashboard.dto.CashFlowResponseDTO;
import com.app.budgets.dashboard.dto.Granularity;

import java.time.LocalDateTime;
import java.util.List;

public interface CashFlowRepositoryCustom {
    List<CashFlowResponseDTO> getCashFlowData(
            String userId,
            Granularity granularity,
            LocalDateTime startDate,
            LocalDateTime endDate
    );
}