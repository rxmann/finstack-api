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
    private static final boolean SECURE = true; // false for local dev if not HTTPS
    private static final int MAX_AGE = 60 * 60 * 24; // 1 day

    @Value("${spring.profiles.active}")
    private String DEPLOYMENT;

    // ---------------- JWT COOKIE ----------------
    public void addJwtCookie(HttpServletResponse response, String jwtToken) {
        var isSecure = DEPLOYMENT == "dev" ? false : true;
        String cookieHeader = String.format(
                "%s=%s; Max-Age=%d; Path=%s; Secure=%b; HttpOnly=%b; SameSite=None",
                COOKIE_NAME, jwtToken, MAX_AGE, COOKIE_PATH, false, HTTP_ONLY);
        response.addHeader("Set-Cookie", cookieHeader);
    }

    public void clearJwtCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie(COOKIE_NAME, null);
        cookie.setHttpOnly(HTTP_ONLY);
        cookie.setSecure(SECURE);
        cookie.setPath(COOKIE_PATH);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
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
