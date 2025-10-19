package com.app.budgets.user.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String email;
    private String username;
    private String passwordHash; // keep for create/update
    private Boolean isActive;
    private Boolean accountLocked;
    private List<String> roles;
    private String providerId;
    private String authProviderType;

}
