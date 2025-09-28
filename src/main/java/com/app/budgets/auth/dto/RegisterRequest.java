package com.app.budgets.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {
    @NotEmpty(message = "First name must not be empty")
    @NotBlank(message = "First name must not be empty")
    private String firstName;

    @NotEmpty(message = "Last name must not be empty")
    @NotBlank(message = "Last name must not be empty")
    private String lastName;

    @NotEmpty(message = "Username must not be empty")
    @NotBlank(message = "Username must not be empty")
    private String username;

    @NotEmpty(message = "Email must not be empty")
    @NotBlank(message = "Email must not be empty")
    @Email
    private String email;

    @NotEmpty(message = "Password must not be empty")
    @NotBlank(message = "Password must not be empty")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

}
