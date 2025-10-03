package com.app.budgets.util;

import java.util.Objects;

import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import com.app.budgets.user.model.AuthProviderType;

@Component
public class OAuth2Util {

    public AuthProviderType getProviderTypeFromRegistrationId(String registrationId) {
        return switch (registrationId.toLowerCase()) {
            case "google" -> AuthProviderType.GOOGLE;
            case "github" -> AuthProviderType.GITHUB;
            default -> throw new IllegalArgumentException("Unsupported OAuth2 provider: " + registrationId);
        };
    }

    public String determineProviderIdFromOAuth2user(OAuth2User oAuth2User, String registrationId) {

        return switch (registrationId.toLowerCase()) {
            case "google" -> Objects.requireNonNull(oAuth2User.getAttribute("sub")).toString();
            case "facebook" -> Objects.requireNonNull(oAuth2User.getAttribute("id")).toString();
            default -> throw new IllegalStateException("Unexpected value: " + registrationId.toLowerCase());
        };
    }

    public String determineUsernameFromOAuth2user(OAuth2User oAuth2User, String registrationId, String providerId) {
        String email = oAuth2User.getAttribute("email");
        if (email != null && !email.isBlank()) {
            return email;
        }

        return switch (registrationId.toLowerCase()) {
            case "google" -> oAuth2User.getAttribute("sub");
            case "github" -> oAuth2User.getAttribute("login");
            default -> providerId;
        };
    }

}
