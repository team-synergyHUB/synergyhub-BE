package com.synergy_hub.synergyhub.auth.oauth.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;


public class CookieService {

    public static Cookie createCookie(String key, String value, Integer expiredS) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(expiredS);
        cookie.setHttpOnly(true);
        cookie.setPath("/");

        return cookie;
    }

    public static void addCookieWithSameSite(HttpServletResponse response, String key, String value, Integer expiredS) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(expiredS);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");

        // 쿠키를 응답에 추가
        response.addCookie(cookie);

        // SameSite 속성을 추가하기 위해 헤더를 직접 설정
        String cookieWithSameSite = String.format("%s=%s; Max-Age=%d; Path=/; HttpOnly; Secure; Domain=34.64.235.3; SameSite=None" ,
                key, value, expiredS);
        response.addHeader("Set-Cookie", cookieWithSameSite);
    }

}
