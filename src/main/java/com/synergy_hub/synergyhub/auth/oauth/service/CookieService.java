package com.synergy_hub.synergyhub.auth.oauth.service;

import jakarta.servlet.http.Cookie;

public class CookieService {

    public static Cookie createCookie(String key, String value, Integer expiredS) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(expiredS);
        cookie.setHttpOnly(true);
        cookie.setPath("/");

        return cookie;
    }
}
