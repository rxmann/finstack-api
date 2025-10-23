package com.app.budgets.paymentreminder.dto;

import com.app.budgets.common.enums.ReminderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateReminderStatusRequest {

    @NotNull(message = "Status is required")
    private ReminderStatus status;
}
