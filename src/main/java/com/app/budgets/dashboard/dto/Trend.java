package com.app.budgets.dashboard.dto;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Trend {
    UP("Up"),
    DOWN("Down"),
    FLAT("Flat");
    private final String value;

    Trend(String value) {
        this.value = value;
    }


    @JsonValue
    public String getValue() {
        return value;
    }

}