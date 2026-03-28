package com.app.budgets.auth.service;

import java.util.List;

import com.app.budgets.util.OAuth2Util;
import com.app.budgets.auth.model.CustomUserDetails;
import com.app.budgets.util.CookieUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.app.budgets.auth.dto.AuthenticationRequest;
import com.app.budgets.auth.dto.AuthenticationResponse;
import com.app.budgets.auth.dto.LoginResponse;
import com.app.budgets.auth.dto.RegisterRequest;
import com.app.budgets.exception.exceptions.UserAlreadyExistsException;
import com.app.budgets.user.UserRepository;
import com.app.budgets.auth.model.AuthProviderType;
import com.app.budgets.user.model.User;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final Logger log = LoggerFactory.getLogger(AuthService.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final OAuth2Util oAuth2Util;
    private final CookieUtil cookieUtil;

    public User signUpInternal(RegisterRequest registerRequest, AuthProviderType authProviderType,
                               String providerId) {

        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User already exists with email: " + registerRequest.getEmail());
        }

        if (userRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException(
                    "User already exists with username: " + registerRequest.getUsername());
        }

        User user = User.builder()
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .passwordHash("grapes")
                .providerId(providerId)
                .authProviderType(authProviderType)
                .roles(List.of(new String[] { "USER" }))
                .build();

        log.debug(user.toString());
        if (authProviderType == AuthProviderType.EMAIL) {
            user.setPasswordHash(passwordEncoder.encode(registerRequest.getPassword()));
        }
        userRepository.save(user);

        return user;
    }

    public AuthenticationResponse signup(RegisterRequest registerRequest) {
        User user = signUpInternal(registerRequest, AuthProviderType.EMAIL, null);
        var userDetails = new CustomUserDetails(user);
        var jwtToken = jwtService.generateToken(userDetails);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request, HttpServletResponse response) {
        var auth = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        // var userDetails = new CustomUserDetails(user);
        var jwtToken = jwtService.generateToken(userDetails);

        cookieUtil.addJwtCookie(response, jwtToken);

        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public ResponseEntity<LoginResponse> handleOAuth2LoginRequest(OAuth2User oAuth2User, String registrationId,
                                                                  HttpServletResponse response) {
        // fetch the provider type and provider id
        AuthProviderType authProviderType = oAuth2Util.getProviderTypeFromRegistrationId(registrationId);
        String providerId = oAuth2Util.determineProviderIdFromOAuth2user(oAuth2User, registrationId);

        User user = userRepository.findByProviderIdAndAuthProviderType(providerId, authProviderType).orElse(null);

        String email = oAuth2User.getAttribute("email");
        User emailUser = userRepository.findByEmail(email).orElse(null);

        if (user == null && emailUser == null) {
            // register user
            String username = oAuth2Util.determineUsernameFromOAuth2user(oAuth2User, registrationId, providerId);
            user = signUpInternal(RegisterRequest.builder().username(username).email(email).build(),
                    authProviderType, providerId);
        } else if (user != null) {
            if (email != null && !email.isBlank() && !email.equals(user.getEmail())) {
                user.setEmail(email);
                userRepository.save(user);
            }
        } else {
            throw new BadCredentialsException(
                    "This email is already registered with provider " + emailUser.getAuthProviderType());
        }

        var userDetails = new CustomUserDetails(user);

        var jwtToken = jwtService.generateToken(userDetails);
        cookieUtil.addJwtCookie(response, jwtToken);

        var loginResponseDto = LoginResponse.builder().jwt(jwtToken).userId(user.getId())
                .build();

        return ResponseEntity.ok(loginResponseDto);
    }
}
