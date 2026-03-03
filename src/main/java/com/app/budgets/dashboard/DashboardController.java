package com.app.budgets.dashboard;

import com.app.budgets.dashboard.dto.response.CashFlowResponse;
import com.app.budgets.dashboard.dto.request.DashboardRequest;
import com.app.budgets.dashboard.dto.response.DashboardResponse;
import com.app.budgets.dashboard.dto.treemap.TreeMapResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
    public ResponseEntity<DashboardResponse> getDashboardAnalytics(@Valid DashboardRequest requestDTO) {
        Optional<DashboardResponse> result = dashboardAnalyticsService.getDashboardAnalytics(requestDTO);
        return result
                .map(r -> ResponseEntity.ok().body(r))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/cashflow")
    public ResponseEntity<List<CashFlowResponse>> getCashflowAnalytics(@Valid DashboardRequest requestDTO) {
        var cfRes = dashboardAnalyticsService.getCashFlowAnalytics(requestDTO);
        return ResponseEntity.ok().body(cfRes);
    }

    @GetMapping("/budget-composition")
    public ResponseEntity<TreeMapResponse> getBudgetSplits(@Valid DashboardRequest requestDTO) {
        var result = dashboardAnalyticsService.getBudgetComposition(requestDTO);
        return ResponseEntity.ok(result);
    }
}
