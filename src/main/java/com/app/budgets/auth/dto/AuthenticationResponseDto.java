package com.app.budgets.auth.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@RequiredArgsConstructor
public class AuthenticationResponseDto {
    private String token;
}
