package com.app.budgets.auth;

import com.app.budgets.auth.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.budgets.auth.dto.AuthenticationRequestDto;
import com.app.budgets.auth.dto.AuthenticationResponseDto;
import com.app.budgets.auth.dto.RegisterRequestDto;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponseDto> register(@Valid @RequestBody RegisterRequestDto request) {
        return ResponseEntity.ok(authService.signup(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDto> login(@Valid @RequestBody AuthenticationRequestDto request,
            HttpServletResponse response) {
        return ResponseEntity.ok(authService.authenticate(request, response));
    }

}
