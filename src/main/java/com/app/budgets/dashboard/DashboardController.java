package com.app.budgets.dashboard;

import com.app.budgets.dashboard.dto.CashFlowResponseDTO;
import com.app.budgets.dashboard.dto.DashboardRequestDTO;
import com.app.budgets.dashboard.dto.DashboardResponseDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/dashboard/analytics")
@Tag(name = "DashboardAnalytics", description = "Dashboard analytics endpoints")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardAnalyticsService dashboardAnalyticsService;

    @GetMapping()
    public ResponseEntity<DashboardResponseDTO> getDashboardAnalytics(@Valid DashboardRequestDTO requestDTO) {
        Optional<DashboardResponseDTO> result = dashboardAnalyticsService.getDashboardAnalytics(requestDTO);
        return result
                .map(r -> ResponseEntity.ok().body(r))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/cashflow")
    public ResponseEntity<List<CashFlowResponseDTO>> getCashflowAnalytics(@Valid DashboardRequestDTO requestDTO) {
        var cfRes = dashboardAnalyticsService.getCashflowAnalytics(requestDTO);
        return ResponseEntity.ok().body(cfRes);
    }
}
