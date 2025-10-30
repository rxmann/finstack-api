package com.app.budgets.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
public class RegisterRequestDto {
    @NotEmpty(message = "Username must not be empty")
    @NotBlank(message = "Username must not be empty")
    @Schema(description = "Username to register.")
    private String username;

    @NotEmpty(message = "Email must not be empty")
    @NotBlank(message = "Email must not be empty")
    @Schema(description = "Unique email")
    @Email
    private String email;

    @NotEmpty(message = "Password must not be empty")
    @NotBlank(message = "Password must not be empty")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Schema(description = "Password of length 8")
    private String password;

}
