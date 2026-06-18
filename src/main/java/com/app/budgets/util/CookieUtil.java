package com.app.budgets.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class CookieUtil {

    private static final String COOKIE_NAME = "access_token";
    private static final String COOKIE_PATH = "/";
    private static final boolean HTTP_ONLY = true;

    @Value("${spring.profiles.active:prod}") // Fallback default to prod if not specified
    private String deployment;

    // Quick side note: In Java, always use .equals() for string comparison!
    private boolean isSecure() {
        return !"dev".equalsIgnoreCase(deployment);
    }

    // ---------------- JWT COOKIE ----------------
    public void addJwtCookie(HttpServletResponse response, String jwtToken) {
        String cookieHeader = String.format(
                "%s=%s; Max-Age=%d; Path=%s; Secure=%b; HttpOnly=%b; SameSite=None",
                COOKIE_NAME, jwtToken, 60 * 60 * 24, COOKIE_PATH, isSecure(), HTTP_ONLY);
        response.addHeader("Set-Cookie", cookieHeader);
    }

    public void clearJwtCookie(HttpServletResponse response) {
        String cookieHeader = String.format(
                "%s=; Max-Age=0; Path=%s; Secure=%b; HttpOnly=%b; SameSite=None",
                COOKIE_NAME, COOKIE_PATH, isSecure(), HTTP_ONLY);
        response.addHeader("Set-Cookie", cookieHeader);
    }

    public String extractJwtFromRequest(HttpServletRequest request) {
        if (request.getCookies() == null)
            return null;
        for (Cookie cookie : request.getCookies()) {
            if (COOKIE_NAME.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
