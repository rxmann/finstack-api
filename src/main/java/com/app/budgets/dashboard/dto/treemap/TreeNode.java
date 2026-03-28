package com.app.budgets.dashboard.dto.treemap;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class TreeNode {
    private String name;
    private BigDecimal value; // null for parent
    private Double percentage;
    private List<TreeNode> children;
}