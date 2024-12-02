package com.synergy_hub.synergyhub.auth.oauth.service;

import com.synergy_hub.synergyhub.global.response.ApiResponse;
import com.synergy_hub.synergyhub.global.response.ApiResponseBuilder;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.Null;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * 쿠키는 만료시키고, token은 헤더에 전달
 */
@Service
@Slf4j
public class Oauth2JwtHeaderService {

    public ResponseEntity<ApiResponse<Void>> handleJwtHeader(HttpServletRequest request, HttpServletResponse response) {

        Cookie[] cookies = request.getCookies();
        String jwtAccessToken = null;

        if (cookies == null) {
            return ApiResponseBuilder.fail("cookie not exist", HttpStatus.BAD_REQUEST);
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("Authorization")) {
                jwtAccessToken = cookie.getValue();
            }
        }

        if (jwtAccessToken == null) {
            return ApiResponseBuilder.fail("cookie not exist", HttpStatus.BAD_REQUEST);
        }

        response.addCookie(CookieService.createCookie("Authorization", null, 0));
        response.addHeader("Authorization", "Bearer " + jwtAccessToken);

        return ApiResponseBuilder.success(
            "JwtHeaderService Successfully", null, HttpStatus.OK);
    }

}
