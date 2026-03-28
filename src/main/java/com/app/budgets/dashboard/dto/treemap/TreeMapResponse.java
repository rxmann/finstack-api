package com.app.budgets.dashboard.dto.treemap;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class TreeMapResponse {
    private String name; // always "Budget Split"
    private List<TreeNode> children;
}