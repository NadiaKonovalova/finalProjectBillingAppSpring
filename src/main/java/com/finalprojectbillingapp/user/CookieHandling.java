package com.finalprojectbillingapp.user;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

public class CookieHandling {
    public static String getUserIdFromCookies (HttpServletRequest request) {
        Cookie [] cookies = request.getCookies();
        String userId = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("loggedInUserId")) {
                    userId = cookie.getValue();
                    break;
                }
            }
        }
        return userId;
    }
}
